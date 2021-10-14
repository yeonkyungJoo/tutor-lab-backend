package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.TutorResponse;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.*;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.*;

@Transactional
@Service
@RequiredArgsConstructor
public class TutorService extends AbstractService {

    private final TutorRepository tutorRepository;

    private final UserRepository userRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final LectureRepository lectureRepository;
    // private final LectureMapstructUtil lectureMapstructUtil;

    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    private Page<Tutor> getTutors(Integer page) {
        return tutorRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public Page<TutorResponse> getTutorResponses(Integer page) {

    }

    @Transactional(readOnly = true)
    private Tutor getTutor(Long tutorId) {
        return tutorRepository.findById(tutorId).orElseThrow(() -> new EntityNotFoundException(TUTOR));
    }

    public Tutor createTutor(User user, TutorSignUpRequest tutorSignUpRequest) {

        // TODO - CHECK
        if (!user.isEmailVerified()) {
            // TODO - throw
        }
        user = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(USER));
        user.setRole(RoleType.TUTOR);

        Tutor tutor = Tutor.of(
                user,
                tutorSignUpRequest.getSubjects(),
                tutorSignUpRequest.isSpecialist()
        );

        tutorSignUpRequest.getCareers().forEach(careerCreateRequest -> {
            Career career = Career.of(
                    tutor,
                    careerCreateRequest.getCompanyName(),
                    careerCreateRequest.getDuty(),
                    LocalDateTimeUtil.getStringToDate(careerCreateRequest.getStartDate()),
                    LocalDateTimeUtil.getStringToDate(careerCreateRequest.getEndDate()),
                    careerCreateRequest.isPresent()
            );
            tutor.addCareer(career);
        });

        tutorSignUpRequest.getEducations().forEach(educationCreateRequest -> {
            Education education = Education.of(
                    tutor,
                    educationCreateRequest.getSchoolName(),
                    educationCreateRequest.getMajor(),
                    LocalDateTimeUtil.getStringToDate(educationCreateRequest.getEntranceDate()),
                    LocalDateTimeUtil.getStringToDate(educationCreateRequest.getGraduationDate()),
                    educationCreateRequest.getScore(),
                    educationCreateRequest.getDegree()
            );
            tutor.addEducation(education);
        });

        tutorRepository.save(tutor);
        return tutor;
    }

    public void updateTutor(User user, TutorUpdateRequest tutorUpdateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
            .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        tutor.setSubjects(tutorUpdateRequest.getSubjects());
        tutor.setSpecialist(tutorUpdateRequest.isSpecialist());
    }

    // TODO - CHECK
    // 튜터 탈퇴 시
    public void deleteTutor(User user) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        tutor.quit();
        tutorRepository.delete(tutor);
    }

    @Transactional(readOnly = true)
    public List<Career> getCareers(Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException(TUTOR));
        return careerRepository.findByTutor(tutor);
    }

    @Transactional(readOnly = true)
    public List<Education> getEducations(Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException(TUTOR));
        return educationRepository.findByTutor(tutor);
    }

    @Transactional(readOnly = true)
    public Page<Lecture> getLectures(User user, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        return lectureRepository.findByTutor(tutor, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public Page<LectureResponse> getLectureResponses(User user, Integer page) {
        // return getLectures(user, page).map(lectureMapstructUtil::getLectureResponse);
        return getLectures(user, page).map(LectureResponse::new);
    }

    @Transactional(readOnly = true)
    public Page<Enrollment> getEnrollmentsOfLecture(User user, Long lectureId, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        return enrollmentRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public Page<Tutee> getTuteesOfLecture(User user, Long lectureId, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
        // TODO - fetch join
        return enrollmentRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(Enrollment::getTutee);
    }

//    @Transactional(readOnly = true)
//    public Page<TuteeResponse> getTuteeResponsesOfLecture(User user, Long lectureId, Integer page) {
//
//        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
//                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));
//
//        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
//                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
//        // TODO - fetch join
//        return enrollmentRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
//                .map(enrollment -> new TuteeResponse(enrollment.getTutee()));
//    }

    @Transactional(readOnly = true)
    public Page<Review> getReviewsOfLecture(User user, Long lectureId, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        return reviewRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

}
