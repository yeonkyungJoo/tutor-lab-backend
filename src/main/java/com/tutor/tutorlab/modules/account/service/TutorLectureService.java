package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorQueryRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentResponse;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE;

@Transactional
@Service
@RequiredArgsConstructor
public class TutorLectureService extends AbstractService {

    private final TutorRepository tutorRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<Lecture> getLectures(User user, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        return lectureRepository.findByTutor(tutor, getPageRequest(page));
    }

    @Transactional(readOnly = true)
    public Page<LectureResponse> getLectureResponses(User user, Integer page) {
        // return getLectures(user, page).map(lectureMapstructUtil::getLectureResponse);
        return getLectures(user, page).map(LectureResponse::new);
    }

    private Page<Enrollment> getEnrollmentsOfLecture(User user, Long lectureId, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        return enrollmentRepository.findByLectureAndCanceledFalseAndClosedFalse(lecture, getPageRequest(page));
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> getEnrollmentResponsesOfLecture(User user, Long lectureId, Integer page) {
        return getEnrollmentsOfLecture(user, lectureId, page).map(EnrollmentResponse::new);
    }

    private Page<Tutee> getTuteesOfLecture(User user, Long lectureId, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
        // TODO - fetch join
        return enrollmentRepository.findByLectureAndCanceledFalseAndClosedFalse(lecture, getPageRequest(page))
                .map(Enrollment::getTutee);
    }

    @Transactional(readOnly = true)
    public Page<TuteeResponse> getTuteeResponsesOfLecture(User user, Long lectureId, Integer page) {
        return getTuteesOfLecture(user, lectureId, page).map(TuteeResponse::new);
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

}
