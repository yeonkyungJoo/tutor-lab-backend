package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.address.util.AddressUtils;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureUpdateRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepositorySupport;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTOR;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LectureServiceImpl extends AbstractService implements LectureService {

    private final LectureRepository lectureRepository;
    private final TutorRepository tutorRepository;
    private final LectureRepositorySupport lectureRepositorySupport;
    private final ReviewRepository reviewRepository;

    // private final LectureMapstructUtil lectureMapstructUtil;

    private Lecture getLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
    }

    @Override
    public LectureResponse getLectureResponse(Long lectureId) {

        // TODO - CHECK : mapstruct vs 생성자
        // return lectureMapstructUtil.getLectureResponse(getLecture(lectureId));
        // return new LectureResponse(getLecture(lectureId));

        // TODO - CHECK : 쿼리 확인
        Lecture lecture = getLecture(lectureId);
        LectureResponse lectureResponse = new LectureResponse(lecture);

        List<Review> reviews = reviewRepository.findByLectureAndEnrollmentIsNotNull(lecture);
        lectureResponse.setReviewCount(reviews.size());
        OptionalDouble scoreAverage = reviews.stream().map(review -> review.getScore()).mapToInt(Integer::intValue).average();
        lectureResponse.setScoreAverage(scoreAverage.isPresent() ? scoreAverage.getAsDouble() : 0);

        lectureResponse.setLectureTutor(getLectureTutorResponse(lecture));
        return lectureResponse;
    }

    private LectureResponse.LectureTutorResponse getLectureTutorResponse(Lecture lecture) {

        Tutor tutor = lecture.getTutor();

        LectureResponse.LectureTutorResponse lectureTutorResponse = new LectureResponse.LectureTutorResponse(tutor);
        lectureTutorResponse.setLectureCount(lectureRepository.countByTutor(tutor));
        lectureTutorResponse.setReviewCount(reviewRepository.countByLectureInAndEnrollmentIsNotNull(lectureRepository.findByTutor(tutor)));
        return lectureTutorResponse;
    }

//    @Override
//    public List<LectureResponse> getLectureResponses(LectureListRequest lectureListRequest) {
//
//        List<LectureResponse> lectures = lectureRepositorySupport.findLecturesBySearch(lectureListRequest).stream()
//                // TODO - CHECK : mapstruct vs 생성자
//                .map(LectureResponse::new).collect(Collectors.toList());
//
//        // TODO - CHECK : 쿼리 확인
//        lectures.forEach(lectureResponse -> {
//            Lecture lecture = getLecture(lectureResponse.getId());
//            List<Review> reviews = reviewRepository.findByLectureAndEnrollmentIsNotNull(lecture);
//            lectureResponse.setReviewCount(reviews.size());
//            OptionalDouble scoreAverage = reviews.stream().map(review -> review.getScore()).mapToInt(Integer::intValue).average();
//            lectureResponse.setScoreAverage(scoreAverage.isPresent() ? scoreAverage.getAsDouble() : 0);
//        });
//
//        return lectures;
//    }

    @Override
    public Page<LectureResponse> getLectureResponses(String zone, LectureListRequest lectureListRequest, Integer page) {

        Page<LectureResponse> lectures = lectureRepositorySupport.findLecturesByZoneAndSearch(
                AddressUtils.convertStringToEmbeddableAddress(zone), lectureListRequest, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(LectureResponse::new);
        // TODO - CHECK : mapstruct vs 생성자

        // TODO - 쿼리
        // TODO - CHECK : 쿼리 확인
        lectures.forEach(lectureResponse -> {
            Lecture lecture = getLecture(lectureResponse.getId());
            List<Review> reviews = reviewRepository.findByLectureAndEnrollmentIsNotNull(lecture);
            lectureResponse.setReviewCount(reviews.size());
            OptionalDouble scoreAverage = reviews.stream().map(review -> review.getScore()).mapToInt(Integer::intValue).average();
            lectureResponse.setScoreAverage(scoreAverage.isPresent() ? scoreAverage.getAsDouble() : 0);

            lectureResponse.setLectureTutor(getLectureTutorResponse(lecture));
        });

        return lectures;
    }

    @Transactional
    @Override
    public Lecture createLecture(User user, LectureCreateRequest lectureCreateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        // TODO 유효성 -> 해당 유저의 강의 갯수 제한?

        Lecture lecture = buildLecture(lectureCreateRequest, tutor);
        for (LectureCreateRequest.LecturePriceCreateRequest lecturePriceRequest : lectureCreateRequest.getLecturePrices()) {
            lecture.addPrice(buildLecturePrice(lecturePriceRequest));
        }

        for (LectureCreateRequest.LectureSubjectCreateRequest subjectRequest : lectureCreateRequest.getSubjects()) {
            lecture.addSubject(buildLectureSubject(subjectRequest));
        }

        return lectureRepository.save(lecture);
    }

    @Transactional
    @Override
    public void updateLecture(User user, Long lectureId, LectureUpdateRequest lectureUpdateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        lecture.getLecturePrices().clear();
        lecture.getLectureSubjects().clear();

        for (LectureUpdateRequest.LecturePriceUpdateRequest lecturePriceUpdateRequest : lectureUpdateRequest.getLecturePrices()) {

            LecturePrice lecturePrice = LecturePrice.of(
                    lecture,
                    lecturePriceUpdateRequest.getIsGroup(),
                    lecturePriceUpdateRequest.getGroupNumber(),
                    lecturePriceUpdateRequest.getTotalTime(),
                    lecturePriceUpdateRequest.getPertimeLecture(),
                    lecturePriceUpdateRequest.getPertimeCost(),
                    lecturePriceUpdateRequest.getTotalCost()
            );
            lecture.addPrice(lecturePrice);
        }

        for (LectureUpdateRequest.LectureSubjectUpdateRequest lectureSubjectUpdateRequest : lectureUpdateRequest.getSubjects()) {

            LectureSubject lectureSubject = LectureSubject.of(
                    lecture,
                    lectureSubjectUpdateRequest.getLearningKindId(),
                    lectureSubjectUpdateRequest.getLearningKind(),
                    lectureSubjectUpdateRequest.getKrSubject()
            );
            lecture.addSubject(lectureSubject);
        }

        lecture.setThumbnail(lectureUpdateRequest.getThumbnailUrl());
        lecture.setTitle(lectureUpdateRequest.getTitle());
        lecture.setSubTitle(lectureUpdateRequest.getSubTitle());
        lecture.setIntroduce(lectureUpdateRequest.getIntroduce());
        lecture.setContent(lectureUpdateRequest.getContent());
        lecture.setDifficultyType(lectureUpdateRequest.getDifficulty());
        lecture.setSystemTypes(lectureUpdateRequest.getSystems());

        /*
        Hibernate: select user0_.user_id as user_id1_17_, user0_.created_at as created_2_17_, user0_.updated_at as updated_3_17_, user0_.bio as bio4_17_, user0_.deleted as deleted5_17_, user0_.deleted_at as deleted_6_17_, user0_.email as email7_17_, user0_.email_verified as email_ve8_17_, user0_.email_verified_at as email_ve9_17_, user0_.email_verify_token as email_v10_17_, user0_.gender as gender11_17_, user0_.image as image12_17_, user0_.name as name13_17_, user0_.nickname as nicknam14_17_, user0_.password as passwor15_17_, user0_.phone_number as phone_n16_17_, user0_.provider as provide17_17_, user0_.provider_id as provide18_17_, user0_.role as role19_17_, user0_.username as usernam20_17_, user0_.zone as zone21_17_ from user user0_ where user0_.username=?
        Hibernate: select user0_.user_id as user_id1_17_, user0_.created_at as created_2_17_, user0_.updated_at as updated_3_17_, user0_.bio as bio4_17_, user0_.deleted as deleted5_17_, user0_.deleted_at as deleted_6_17_, user0_.email as email7_17_, user0_.email_verified as email_ve8_17_, user0_.email_verified_at as email_ve9_17_, user0_.email_verify_token as email_v10_17_, user0_.gender as gender11_17_, user0_.image as image12_17_, user0_.name as name13_17_, user0_.nickname as nicknam14_17_, user0_.password as passwor15_17_, user0_.phone_number as phone_n16_17_, user0_.provider as provide17_17_, user0_.provider_id as provide18_17_, user0_.role as role19_17_, user0_.username as usernam20_17_, user0_.zone as zone21_17_ from user user0_ where user0_.username=?
        Hibernate: select tutor0_.tutor_id as tutor_id1_15_, tutor0_.created_at as created_2_15_, tutor0_.updated_at as updated_3_15_, tutor0_.specialist as speciali4_15_, tutor0_.subjects as subjects5_15_, tutor0_.user_id as user_id6_15_ from tutor tutor0_ where tutor0_.user_id=?
        Hibernate: select lecture0_.lecture_id as lecture_1_6_, lecture0_.created_at as created_2_6_, lecture0_.updated_at as updated_3_6_, lecture0_.content as content4_6_, lecture0_.difficulty_type as difficul5_6_, lecture0_.introduce as introduc6_6_, lecture0_.sub_title as sub_titl7_6_, lecture0_.thumbnail as thumbnai8_6_, lecture0_.title as title9_6_, lecture0_.tutor_id as tutor_i10_6_ from lecture lecture0_ where lecture0_.tutor_id=? and lecture0_.lecture_id=?
        Hibernate: select lecturepri0_.lecture_id as lecture10_7_1_, lecturepri0_.lecture_price_id as lecture_1_7_1_, lecturepri0_.lecture_price_id as lecture_1_7_0_, lecturepri0_.created_at as created_2_7_0_, lecturepri0_.updated_at as updated_3_7_0_, lecturepri0_.group_number as group_nu4_7_0_, lecturepri0_.is_group as is_group5_7_0_, lecturepri0_.lecture_id as lecture10_7_0_, lecturepri0_.pertime_cost as pertime_6_7_0_, lecturepri0_.pertime_lecture as pertime_7_7_0_, lecturepri0_.total_cost as total_co8_7_0_, lecturepri0_.total_time as total_ti9_7_0_ from lecture_price lecturepri0_ where lecturepri0_.lecture_id=?
        Hibernate: select lecturesub0_.lecture_id as lecture_6_8_1_, lecturesub0_.lecture_subject_id as lecture_1_8_1_, lecturesub0_.lecture_subject_id as lecture_1_8_0_, lecturesub0_.created_at as created_2_8_0_, lecturesub0_.updated_at as updated_3_8_0_, lecturesub0_.kr_subject as kr_subje4_8_0_, lecturesub0_.lecture_id as lecture_6_8_0_, lecturesub0_.parent as parent5_8_0_ from lecture_subject lecturesub0_ where lecturesub0_.lecture_id=?
        Hibernate: insert into lecture_price (created_at, group_number, is_group, lecture_id, pertime_cost, pertime_lecture, total_cost, total_time) values (?, ?, ?, ?, ?, ?, ?, ?)
        Hibernate: insert into lecture_subject (created_at, kr_subject, lecture_id, parent) values (?, ?, ?, ?)
        Hibernate: update lecture set updated_at=?, content=?, difficulty_type=?, introduce=?, sub_title=?, thumbnail=?, title=?, tutor_id=? where lecture_id=?
        Hibernate: delete from lecture_system_type where lecture_id=?
        Hibernate: insert into lecture_system_type (lecture_id, system_types) values (?, ?)
        Hibernate: delete from lecture_price where lecture_price_id=?
        Hibernate: delete from lecture_subject where lecture_subject_id=?
         */
    }

    @Transactional
    @Override
    public void deleteLecture(User user, Long lectureId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO - CHECK : vs delete(lecture);
        // lecture_price
        // lecture_subject
        // lecture_system_type
        lectureRepository.delete(lecture);
        // lectureRepository.deleteById(lectureId);
    }

    private LectureSubject buildLectureSubject(LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest) {
        return LectureSubject.of(
                null,
                lectureSubjectCreateRequest.getLearningKindId(),
                lectureSubjectCreateRequest.getLearningKind(),
                lectureSubjectCreateRequest.getKrSubject()
        );
    }

    private LecturePrice buildLecturePrice(LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest) {
        return LecturePrice.of(
                null,
                lecturePriceCreateRequest.getIsGroup(),
                lecturePriceCreateRequest.getGroupNumber(),
                lecturePriceCreateRequest.getTotalTime(),
                lecturePriceCreateRequest.getPertimeLecture(),
                lecturePriceCreateRequest.getPertimeCost(),
                lecturePriceCreateRequest.getTotalCost()
        );
    }

    private Lecture buildLecture(LectureCreateRequest lectureCreateRequest, Tutor tutor) {
        return Lecture.of(
                tutor,
                lectureCreateRequest.getTitle(),
                lectureCreateRequest.getSubTitle(),
                lectureCreateRequest.getIntroduce(),
                lectureCreateRequest.getContent(),
                lectureCreateRequest.getDifficulty(),
                lectureCreateRequest.getSystems(),
                lectureCreateRequest.getThumbnailUrl()
        );
    }
}
