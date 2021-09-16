package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.*;

@Transactional(readOnly = false)
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LectureRepository lectureRepository;

    private final EnrollmentRepository enrollmentRepository;

    public Review createTutorReview(Tutor tutor, Long lectureId, Long parentId, TutorReviewCreateRequest tutorReviewCreateRequest) {

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

        // TODO - CHECK
        // id check
        parent.addChild(review);

        return reviewRepository.save(review);
    }

    public void updateTutorReview(Tutor tutor, Long lectureId, Long parentId, Long reviewId, TutorReviewUpdateRequest tutorReviewUpdateRequest) {

        // 1. 해당 튜터의 강의인가?
        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO
        // 2. 해당 강의의 리뷰인가?
        Review parent = reviewRepository.findByLectureAndId(lecture, parentId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // 3. 해당 리뷰에 대한 댓글이 맞는가?
        Review review = reviewRepository.findByParentAndId(parent, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        review.setContent(tutorReviewUpdateRequest.getContent());
    }

    public void deleteTutorReview(Tutor tutor, Long lectureId, Long parentId, Long reviewId) {

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Review parent = reviewRepository.findByLectureAndId(lecture, parentId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));
        // TODO - CHECK : vs findByParentId
        Review review = reviewRepository.findByParentAndId(parent, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // TODO - CHECK : delete
        // TODO - CHECK : CASCADE
        parent.getChildren().remove(review);

        // TODO - delete 시에 id로 먼저 조회
        reviewRepository.delete(review);
    }

    public Review createTuteeReview(Tutee tutee, Long lectureId, TuteeReviewCreateRequest tuteeReviewCreateRequest) {

        // TODO : CHECK : vs enrollementRepository.findByTuteeAndLectureId(tutee, lectureId);
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO - Optional
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
        if (enrollment == null) {
            throw new EntityNotFoundException(ENROLLMENT);
        }

        // TODO - Builder
        // TODO - TEST
        Review review = new Review();
        review.setScore(tuteeReviewCreateRequest.getScore());
        review.setContent(tuteeReviewCreateRequest.getContent());
        review.setEnrollment(enrollment);
        review.setLecture(lecture);
        review.setUser(tutee.getUser());

        return reviewRepository.save(review);
    }

    public void updateTuteeReview(Tutee tutee, Long lectureId, Long reviewId, TuteeReviewUpdateRequest tuteeReviewUpdateRequest) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO - Optional
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
        if (enrollment == null) {
            throw new EntityNotFoundException(ENROLLMENT);
        }

        // TODO - 튜티 자신의 리뷰가 맞는지 확인 필요
        // TODO - CHECK
        Review review = reviewRepository.findByEnrollmentAndId(enrollment, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));
//        Review review = reviewRepository.findByLectureAndId(lecture, reviewId)
//                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        review.setContent(tuteeReviewUpdateRequest.getContent());
        review.setScore(tuteeReviewUpdateRequest.getScore());
    }

    public void deleteTuteeReview(Tutee tutee, Long lectureId, Long reviewId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO - Optional
        // 1. 튜티가 해당 강의를 구매했는지 체크
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
        if (enrollment == null) {
            throw new EntityNotFoundException(ENROLLMENT);
        }

        // TODO - 튜티 자신의 리뷰가 맞는지 확인 필요
        // 2. 수강 내역 - 리뷰
        Review review = reviewRepository.findByEnrollmentAndId(enrollment, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // TODO - CHECK
        // 해당 강의의 리뷰가 맞는가?
//        Review review = reviewRepository.findByLectureAndId(lecture, reviewId)
//                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        // TODO - children 삭제 체크
        reviewRepository.delete(review);
    }

}
