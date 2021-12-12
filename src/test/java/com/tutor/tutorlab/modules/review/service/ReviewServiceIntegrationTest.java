package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.LearningKindType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.vo.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewServiceIntegrationTest extends AbstractTest {

    private User tutorUser;
    private Tutor tutor;
    private Lecture lecture1;
    private Long lecture1Id;
    private Lecture lecture2;
    private Long lecture2Id;

    @BeforeEach
    void init() {

        SignUpRequest signUpRequest = getSignUpRequest("tutor", "tutor");
        tutorUser = loginService.signUp(signUpRequest);
        loginService.verifyEmail(tutorUser.getUsername(), tutorUser.getEmailVerifyToken());
        tutor = tutorService.createTutor(tutorUser, tutorSignUpRequest);

        lecture1 = lectureService.createLecture(tutorUser, lectureCreateRequest);
        lecture1Id = lecture1.getId();

        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest2
                = LectureCreateRequest.LecturePriceCreateRequest.of(false, 3, 1000L, 3, 10, 30000L);
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest2
                = LectureCreateRequest.LectureSubjectCreateRequest.of(LearningKindType.IT, "자바스크립트");
        LectureCreateRequest lectureCreateRequest2 = LectureCreateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "제목2",
                "소제목2",
                "소개2",
                DifficultyType.INTERMEDIATE,
                "<p>본문2</p>",
                Arrays.asList(SystemType.OFFLINE),
                Arrays.asList(lecturePriceCreateRequest2),
                Arrays.asList(lectureSubjectCreateRequest2)
        );
        lecture2 = lectureService.createLecture(tutorUser, lectureCreateRequest2);
        lecture2Id = lecture2.getId();
    }

    @WithAccount(NAME)
    @DisplayName("튜티 리뷰 등록")
    @Test
    void createTuteeReview() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);

        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
        assertEquals(1, enrollmentRepository.findByTuteeAndCanceledFalseAndClosedFalse(tutee).size());
        assertNull(cancellationRepository.findByEnrollment(enrollment));
        assertNotNull(chatroomRepository.findByEnrollment(enrollment));

        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        Long chatroomId = chatroom.getId();

        // When
        reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // Then
        Review review = reviewRepository.findByEnrollment(enrollment);
        assertNotNull(review);
        assertAll(
                () -> assertEquals(enrollment, review.getEnrollment()),
                () -> assertEquals(0, review.getChildren().size()),
                () -> assertEquals(lecture1, review.getLecture()),
                () -> assertEquals(tuteeReviewCreateRequest.getContent(), review.getContent()),
                () -> assertEquals(tuteeReviewCreateRequest.getScore(), review.getScore())
        );
    }

    // TODO - 튜티가 종료하는 것으로 변경
//    @WithAccount(NAME)
//    @DisplayName("튜티 리뷰 등록 - 종료된 강의")
//    @Test
//    void createTuteeReview_withClosedLecture() {
//
//        // Given
//        User user = userRepository.findByUsername(USERNAME).orElse(null);
//        Tutee tutee = tuteeRepository.findByUser(user);
//        assertNotNull(user);
//
//        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
//
//        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
//        assertEquals(1, enrollmentRepository.findByTutee(tutee).size());
//        assertNull(cancellationRepository.findByEnrollment(enrollment));
//        assertNotNull(chatroomRepository.findByEnrollment(enrollment));
//
//        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
//        Long chatroomId = chatroom.getId();
//
//        // 수강 종료
//        enrollmentService.close(tutorUser, lecture1Id, enrollment.getId());
//
//        // When
//        reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);
//
//        // Then
//        Review review = reviewRepository.findByEnrollment(enrollment);
//        assertNotNull(review);
//        assertAll(
//                () -> assertEquals(enrollment, review.getEnrollment()),
//                () -> assertEquals(0, review.getChildren().size()),
//                () -> assertEquals(lecture1, review.getLecture()),
//                () -> assertEquals(tuteeReviewCreateRequest.getContent(), review.getContent()),
//                () -> assertEquals(tuteeReviewCreateRequest.getScore(), review.getScore())
//        );
//    }

    @WithAccount(NAME)
    @DisplayName("튜티 리뷰 등록 - 취소한 강의")
    @Test
    void createTuteeReview_withCanceledLecture() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);

        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
        assertEquals(1, enrollmentRepository.findByTuteeAndCanceledFalseAndClosedFalse(tutee).size());
        assertNull(cancellationRepository.findByEnrollment(enrollment));
        assertNotNull(chatroomRepository.findByEnrollment(enrollment));

        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        Long chatroomId = chatroom.getId();

        cancellationService.cancel(user, lecture1Id, cancellationCreateRequest);
        assertNotNull(cancellationRepository.findByEnrollment(enrollment));

        // When
        reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // Then
        Review review = reviewRepository.findByEnrollment(enrollment);
        assertNotNull(review);
        assertAll(
                () -> assertEquals(enrollment, review.getEnrollment()),
                () -> assertEquals(0, review.getChildren().size()),
                () -> assertEquals(lecture1, review.getLecture()),
                () -> assertEquals(tuteeReviewCreateRequest.getContent(), review.getContent()),
                () -> assertEquals(tuteeReviewCreateRequest.getScore(), review.getScore())
        );
    }

    @WithAccount(NAME)
    @DisplayName("튜티 리뷰 등록 - 수강 강의가 아닌 경우")
    @Test
    void createTuteeReview_unEnrolled() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);

        // When
        // Then
        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.createTuteeReview(user, lecture2Id, tuteeReviewCreateRequest);
        });
    }

    @WithAccount(NAME)
    @DisplayName("튜티 리뷰 수정")
    @Test
    void updateTuteeReview() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
        Review review = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // When
        reviewService.updateTuteeReview(user, lecture1Id, review.getId(), tuteeReviewUpdateRequest);

        // Then
        Review updatedReview = reviewRepository.findByEnrollment(enrollment);
        assertNotNull(updatedReview);
        assertAll(
                () -> assertEquals(enrollment, updatedReview.getEnrollment()),
                () -> assertEquals(0, updatedReview.getChildren().size()),
                () -> assertEquals(lecture1, updatedReview.getLecture()),
                () -> assertEquals(tuteeReviewUpdateRequest.getContent(), updatedReview.getContent()),
                () -> assertEquals(tuteeReviewUpdateRequest.getScore(), updatedReview.getScore())
        );
    }

    @WithAccount(NAME)
    @DisplayName("튜티 리뷰 삭제")
    @Test
    void deleteTuteeReview() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
        Review review = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);
        assertEquals(1, reviewRepository.findByLecture(lecture1).size());

        // When
        reviewService.deleteTuteeReview(user, lecture1Id, review.getId());

        // Then
        assertEquals(0, reviewRepository.findByLecture(lecture1).size());

    }

    @WithAccount(NAME)
    @DisplayName("튜터 리뷰 등록")
    @Test
    void createTutorReview() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
        Review parent = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // When
        Review child = reviewService.createTutorReview(tutorUser, lecture1Id, parent.getId(), tutorReviewCreateRequest);

        // Then
        List<Review> reviews = reviewRepository.findByLecture(lecture1);
        assertEquals(2, reviews.size());

        Review review = reviewRepository.findByParentAndId(parent, child.getId()).orElse(null);
        assertNotNull(review);
        assertAll(
                () -> assertEquals(parent, review.getParent()),
                () -> assertEquals(child, review),
                () -> assertEquals(tutorReviewCreateRequest.getContent(), review.getContent()),
                () -> assertEquals(lecture1, review.getLecture()),
                () -> assertNull(review.getEnrollment())
        );
    }

    @WithAccount(NAME)
    @DisplayName("튜티 리뷰 삭제 - 튜터가 댓글을 단 경우")
    @Test
    void deleteTuteeReview_withChildren() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
        Review parent = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);
        Review child = reviewService.createTutorReview(tutorUser, lecture1Id, parent.getId(), tutorReviewCreateRequest);

        // When
        reviewService.deleteTuteeReview(user, lecture1Id, parent.getId());

        // Then
        // children 삭제 체크
        List<Review> reviews = reviewRepository.findByLecture(lecture1);
        assertEquals(0, reviews.size());
        assertFalse(reviewRepository.findById(parent.getId()).isPresent());
        assertFalse(reviewRepository.findById(child.getId()).isPresent());
    }

    @WithAccount(NAME)
    @DisplayName("튜터 리뷰 수정")
    @Test
    void updateTutorReview() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
        Review parent = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);
        Review child = reviewService.createTutorReview(tutorUser, lecture1Id, parent.getId(), tutorReviewCreateRequest);

        // When
        reviewService.updateTutorReview(tutorUser, lecture1Id, parent.getId(), child.getId(), tutorReviewUpdateRequest);

        // Then
        List<Review> reviews = reviewRepository.findByLecture(lecture1);
        assertEquals(2, reviews.size());

        Review review = reviewRepository.findByParentAndId(parent, child.getId()).orElse(null);
        assertNotNull(review);
        assertAll(
                () -> assertEquals(parent, review.getParent()),
                () -> assertEquals(child, review),
                () -> assertEquals(tutorReviewUpdateRequest.getContent(), review.getContent()),
                () -> assertEquals(lecture1, review.getLecture()),
                () -> assertNull(review.getEnrollment())
        );

    }

    @WithAccount(NAME)
    @DisplayName("튜터 리뷰 삭제")
    @Test
    void deleteTutorReview() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.createEnrollment(user, lecture1Id, lecturePrice1.getId());
        Review parent = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);
        Review child = reviewService.createTutorReview(tutorUser, lecture1Id, parent.getId(), tutorReviewCreateRequest);

        // When
        reviewService.deleteTutorReview(tutorUser, lecture1Id, parent.getId(), child.getId());

        // Then
        // List<Review> reviews = reviewRepository.findByLecture(lecture1);
        // assertEquals(1, reviews.size());
        assertTrue(reviewRepository.findById(parent.getId()).isPresent());

        // parent = reviewRepository.findById(parent.getId()).orElse(null);
        assertEquals(reviewRepository.findByEnrollment(enrollment), reviewRepository.findByLecture(lecture1).get(0));
        parent = reviewRepository.findByLecture(lecture1).get(0);
        assertEquals(0, parent.getChildren().size());
        assertFalse(reviewRepository.findById(child.getId()).isPresent());
    }
}