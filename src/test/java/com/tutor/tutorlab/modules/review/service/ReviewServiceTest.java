package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import org.assertj.core.internal.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;
    @Mock
    ReviewRepository reviewRepository;

    @Mock
    TuteeRepository tuteeRepository;
    @Mock
    LectureRepository lectureRepository;
    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    TutorRepository tutorRepository;

    @Test
    void createTuteeReview() {
        // user(tutee), enrollmentId, tuteeReviewCreateRequest
        // user(tutee), lectureId, tuteeReviewCreateRequest

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tutee.getId()).thenReturn(1L);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Lecture lecture = Mockito.mock(Lecture.class);
        // when(lecture.getId()).thenReturn(1L);
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollmentRepository.findAllByTuteeIdAndLectureId(1L, 1L))
                .thenReturn(Optional.of(enrollment));

        // when
        User user = Mockito.mock(User.class);
        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.of(4, "good");
        reviewService.createTuteeReview(user, 1L, tuteeReviewCreateRequest);

        // then
        verify(reviewRepository).save(Review.buildTuteeReview(user, lecture, enrollment, tuteeReviewCreateRequest));
    }

    @Test
    void updateTuteeReview() {
        // user(tutee), lectureId, reviewId, tuteeReviewUpdateRequest

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tutee.getId()).thenReturn(1L);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Lecture lecture = Mockito.mock(Lecture.class);
        // when(lecture.getId()).thenReturn(1L);
        // when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollmentRepository.findAllByTuteeIdAndLectureId(1L, 1L))
                .thenReturn(Optional.of(enrollment));

        Review review = Mockito.mock(Review.class);
        // when(review.getId()).thenReturn(1L);
        when(reviewRepository.findByEnrollmentAndId(enrollment, 1L))
                .thenReturn(Optional.of(review));

        // when
        User user = Mockito.mock(User.class);
        TuteeReviewUpdateRequest tuteeReviewUpdateRequest = Mockito.mock(TuteeReviewUpdateRequest.class);
        reviewService.updateTuteeReview(user, 1L, 1L, tuteeReviewUpdateRequest);

        // then
        verify(review).updateTuteeReview(tuteeReviewUpdateRequest);
    }

    @Test
    void deleteTuteeReview() {
        // user(tutee), lectureId, reviewId

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tutee.getId()).thenReturn(1L);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Lecture lecture = Mockito.mock(Lecture.class);
        // when(lecture.getId()).thenReturn(1L);
        // when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollmentRepository.findAllByTuteeIdAndLectureId(1L, 1L))
                .thenReturn(Optional.of(enrollment));

        Review review = Mockito.mock(Review.class);
        // when(review.getId()).thenReturn(1L);
        when(reviewRepository.findByEnrollmentAndId(enrollment, 1L))
                .thenReturn(Optional.of(review));

        // when
        User user = Mockito.mock(User.class);
        reviewService.deleteTuteeReview(user, 1L, 1L);

        // then
        // 댓글 리뷰 삭제
        verify(review).delete();
        verify(reviewRepository).delete(review);
    }

    @Test
    void createTutorReview() {
        // user(tutor), lectureId, parentId, tutorReviewCreateRequest

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(any(User.class))).thenReturn(tutor);

        Lecture lecture = Mockito.mock(Lecture.class);
        when(lectureRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(lecture));

        Review parent = Mockito.mock(Review.class);
        when(reviewRepository.findByLectureAndId(lecture, 1L)).thenReturn(Optional.of(parent));

        // when
        User user = Mockito.mock(User.class);
        TutorReviewCreateRequest tutorReviewCreateRequest = Mockito.mock(TutorReviewCreateRequest.class);
        reviewService.createTutorReview(user, 1L, 1L, tutorReviewCreateRequest);

        // then
        verify(reviewRepository).save(Review.buildTutorReview(user, lecture, parent, tutorReviewCreateRequest));
    }

    @Test
    void updateTutorReview() {
        // user(tutor), lectureId, parentId, reviewId, tutorReviewUpdateRequest

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(any(User.class))).thenReturn(tutor);

        Lecture lecture = Mockito.mock(Lecture.class);
        when(lectureRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(lecture));

        Review parent = Mockito.mock(Review.class);
        when(reviewRepository.findByLectureAndId(lecture, 1L)).thenReturn(Optional.of(parent));
        Review review = Mockito.mock(Review.class);
        when(reviewRepository.findByParentAndId(parent, 2L)).thenReturn(Optional.of(review));

        // when
        User user = Mockito.mock(User.class);
        TutorReviewUpdateRequest tutorReviewUpdateRequest = Mockito.mock(TutorReviewUpdateRequest.class);
        reviewService.updateTutorReview(user, 1L, 1L, 2L, tutorReviewUpdateRequest);

        // then
        verify(review).updateTutorReview(tutorReviewUpdateRequest);
    }

    @Test
    void deleteTutorReview() {
        // user(tutor), lecturId, parentId, reviewId

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(any(User.class))).thenReturn(tutor);

        Lecture lecture = Mockito.mock(Lecture.class);
        when(lectureRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(lecture));

        Review parent = Mockito.mock(Review.class);
        when(reviewRepository.findByLectureAndId(lecture, 1L)).thenReturn(Optional.of(parent));
        Review review = Mockito.mock(Review.class);
        when(reviewRepository.findByParentAndId(parent, 2L)).thenReturn(Optional.of(review));

        // when
        User user = Mockito.mock(User.class);
        reviewService.deleteTutorReview(user, 1L, 1L, 2L);

        // then
        verify(review).delete();
        verify(reviewRepository).delete(review);
    }


    @Test
    void getReviewResponse_withoutChild() throws IllegalAccessException, InstantiationException {
        // reviewId

        // given
        Review parent = Review.buildTuteeReview(
                Mockito.mock(User.class),
                Mockito.mock(Lecture.class),
                Mockito.mock(Enrollment.class),
                Mockito.mock(TuteeReviewCreateRequest.class)
        );
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(reviewRepository.findByParent(parent)).thenReturn(Optional.empty());

        // when
        // then
        ReviewResponse reviewResponse = reviewService.getReviewResponse(1L);
        System.out.println(reviewResponse);
    }

    @Test
    void getReviewResponse_withChild() {

        // given
        Review parent = Review.buildTuteeReview(
                Mockito.mock(User.class),
                Mockito.mock(Lecture.class),
                Mockito.mock(Enrollment.class),
                TuteeReviewCreateRequest.of(4, "tutee content")
        );
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(parent));

        Review child = Review.buildTutorReview(
                Mockito.mock(User.class),
                Mockito.mock(Lecture.class),
                parent,
                TutorReviewCreateRequest.of("tutor content")
        );
        when(reviewRepository.findByParent(parent)).thenReturn(Optional.of(child));

        // when
        // then
        ReviewResponse reviewResponse = reviewService.getReviewResponse(1L);
        System.out.println(reviewResponse);
    }
}