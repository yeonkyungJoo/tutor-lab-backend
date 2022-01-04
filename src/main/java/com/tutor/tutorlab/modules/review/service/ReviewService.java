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
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.controller.response.ReviewWithSimpleLectureResponse;
import com.tutor.tutorlab.modules.review.repository.ReviewQueryRepository;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final ReviewQueryRepository reviewQueryRepository;

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

        return reviewRepository.save(Review.buildTutorReview(user, lecture, parent, tutorReviewCreateRequest));
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

        review.updateTutorReview(tutorReviewUpdateRequest);
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

        review.delete();
        // TODO - delete 시에 id로 먼저 조회
        reviewRepository.delete(review);
    }

    public Review createTuteeReview(User user, Long lectureId, TuteeReviewCreateRequest tuteeReviewCreateRequest) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // 종료/취소된 강의 리뷰 가능
        Enrollment enrollment = enrollmentRepository.findAllByTuteeIdAndLectureId(tutee.getId(), lectureId)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT));

        return reviewRepository.save(Review.buildTuteeReview(user, lecture, enrollment, tuteeReviewCreateRequest));
    }

    public void updateTuteeReview(User user, Long lectureId, Long reviewId, TuteeReviewUpdateRequest tuteeReviewUpdateRequest) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

//        Lecture lecture = lectureRepository.findById(lectureId)
//                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // 종료/취소된 강의 리뷰 가능
        Enrollment enrollment = enrollmentRepository.findAllByTuteeIdAndLectureId(tutee.getId(), lectureId)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT));

        Review review = reviewRepository.findByEnrollmentAndId(enrollment, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        review.updateTuteeReview(tuteeReviewUpdateRequest);
    }

    public void deleteTuteeReview(User user, Long lectureId, Long reviewId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

//        Lecture lecture = lectureRepository.findById(lectureId)
//                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // 종료/취소된 강의 리뷰 가능
        Enrollment enrollment = enrollmentRepository.findAllByTuteeIdAndLectureId(tutee.getId(), lectureId)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT));

        Review review = reviewRepository.findByEnrollmentAndId(enrollment, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        review.delete();
        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewResponsesOfLecture(Long lectureId, Integer page) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
        return reviewQueryRepository.findReviewsWithChildByLecture(lecture, getPageRequest(page));
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewResponseOfLecture(Long lectureId, Long reviewId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Review parent = reviewRepository.findByLectureAndId(lecture, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // TODO - Optional 체크
        Optional<Review> child = reviewRepository.findByParent(parent);
        return new ReviewResponse(parent, child.orElse(null));
    }

    @Transactional(readOnly = true)
    public Page<ReviewWithSimpleLectureResponse> getReviewWithSimpleLectureResponses(User user, Integer page) {
        return reviewQueryRepository.findReviewsWithChildAndSimpleLectureByUser(user, getPageRequest(page));
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewResponse(Long reviewId) {

        Review parent = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // TODO - Optional 체크
        Optional<Review> child = reviewRepository.findByParent(parent);
        return new ReviewResponse(parent, child.orElse(null));
    }
}
