package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.*;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTEE;

@Transactional
@RequiredArgsConstructor
@Service
public class ReviewService extends AbstractService {

    private final ReviewRepository reviewRepository;

    private final TuteeRepository tuteeRepository;
    private final TutorRepository tutorRepository;
    private final LectureRepository lectureRepository;

    private final EnrollmentRepository enrollmentRepository;

    public Review createTutorReview(User user, Long lectureId, Long parentId, TutorReviewCreateRequest tutorReviewCreateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        // 1. 해당 튜터의 강의인가?
        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // 2. 해당 강의의 리뷰인가?
        Review parent = reviewRepository.findByLectureAndId(lecture, parentId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // TODO - Builder
        Review review = new Review();
        review.setContent(tutorReviewCreateRequest.getContent());
        review.setLecture(lecture);
        review.setUser(tutor.getUser());
        review.setParent(parent);
        // TODO - CHECK : id check
        parent.addChild(review);

        return reviewRepository.save(review);
    }

    public void updateTutorReview(User user, Long lectureId, Long parentId, Long reviewId, TutorReviewUpdateRequest tutorReviewUpdateRequest) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        // 1. 해당 튜터의 강의인가?
        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // 2. 해당 강의의 리뷰인가?
        Review parent = reviewRepository.findByLectureAndId(lecture, parentId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // 3. 해당 리뷰에 대한 댓글이 맞는가?
        Review review = reviewRepository.findByParentAndId(parent, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        review.setContent(tutorReviewUpdateRequest.getContent());
    }

    public void deleteTutorReview(User user, Long lectureId, Long parentId, Long reviewId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Review parent = reviewRepository.findByLectureAndId(lecture, parentId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // TODO - CHECK : vs findByParentId
        Review review = reviewRepository.findByParentAndId(parent, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // TODO - CHECK : mappedBy된 리스트 값
        parent.getChildren().remove(review);
        review.delete();
        // TODO - delete 시에 id로 먼저 조회
        reviewRepository.delete(review);
    }

    // 수강 강의별 리뷰 조회 : getReviewsOfLecture
    @Transactional(readOnly = true)
    public Page<Review> getReviewsOfLecture(Long lectureId, Integer page) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
        return reviewRepository.findByLecture(lecture, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    // getReviewOfLecture
    @Transactional(readOnly = true)
    public Review getReview(Long lectureId, Long reviewId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        return reviewRepository.findByLectureAndId(lecture, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));
    }

    public Review createTuteeReview(User user, Long lectureId, TuteeReviewCreateRequest tuteeReviewCreateRequest) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO : CHECK : vs enrollmentRepository.findByTuteeAndLectureId(tutee, lectureId);
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT));

        // TODO - Builder
        // TODO - TEST
        Review review = new Review();
        review.setScore(tuteeReviewCreateRequest.getScore());
        review.setContent(tuteeReviewCreateRequest.getContent());
        review.setEnrollment(enrollment);
        review.setLecture(lecture);
        // TODO - DEBUG
        // TODO - CHECK : review.setUser(user);
        review.setUser(tutee.getUser());

        return reviewRepository.save(review);
    }

    public void updateTuteeReview(User user, Long lectureId, Long reviewId, TuteeReviewUpdateRequest tuteeReviewUpdateRequest) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO : CHECK : vs enrollmentRepository.findByTuteeAndLectureId(tutee, lectureId);
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT));

        Review review = reviewRepository.findByEnrollmentAndId(enrollment, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        review.setContent(tuteeReviewUpdateRequest.getContent());
        review.setScore(tuteeReviewUpdateRequest.getScore());
    }

    public void deleteTuteeReview(User user, Long lectureId, Long reviewId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO : CHECK : vs enrollmentRepository.findByTuteeAndLectureId(tutee, lectureId);
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT));

        Review review = reviewRepository.findByEnrollmentAndId(enrollment, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        review.delete();
        reviewRepository.delete(review);
    }

}
