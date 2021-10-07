package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.common.LectureBuilder;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewService reviewService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TuteeRepository tuteeRepository;

    @Autowired
    LoginService loginService;
    @Autowired
    TutorService tutorService;
    @Autowired
    LectureService lectureService;
    @Autowired
    LecturePriceRepository lecturePriceRepository;

    @Autowired
    EnrollmentService enrollmentService;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    CancellationRepository cancellationRepository;

    @Autowired
    ChatroomRepository chatroomRepository;

    private User tutorUser;
    private Tutor tutor;
    private Lecture lecture1;
    private Long lecture1Id;
    private Lecture lecture2;
    private Long lecture2Id;

    @BeforeEach
    void init() {

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk2@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk2")
                .gender("FEMALE")
                .build();
        tutorUser = loginService.signUp(signUpRequest);
        loginService.verifyEmail(tutorUser.getUsername(), tutorUser.getEmailVerifyToken());

        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();

        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorSignUpRequest.addCareerCreateRequest(careerCreateRequest);
        tutorSignUpRequest.addEducationCreateRequest(educationCreateRequest);
        tutor = tutorService.createTutor(tutorUser, tutorSignUpRequest);

        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest1
                = LectureBuilder.getLecturePriceCreateRequest(true, 3, 1000L, 3, 3000L, 10);
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest1
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바");

        LectureCreateRequest lectureCreateRequest1 = LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목")
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(lecturePriceCreateRequest1))
                .subjects(Arrays.asList(lectureSubjectCreateRequest1))
                .build();
        lecture1 = lectureService.createLecture(tutorUser, lectureCreateRequest1);
        lecture1Id = lecture1.getId();

        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest2
                = LectureBuilder.getLecturePriceCreateRequest(false, 3, 1000L, 3, 30000L, 10);
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest2
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바스크립트");

        LectureCreateRequest lectureCreateRequest2 = LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목")
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(lecturePriceCreateRequest2))
                .subjects(Arrays.asList(lectureSubjectCreateRequest2))
                .build();
        lecture2 = lectureService.createLecture(tutorUser, lectureCreateRequest2);
        lecture2Id = lecture2.getId();
    }

    @WithAccount("yk")
    @DisplayName("튜티 리뷰 등록")
    @Test
    void createTuteeReview() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);

        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());
        assertEquals(1, enrollmentRepository.findByTutee(tutee).size());
        assertEquals(0, cancellationRepository.findByTutee(tutee).size());
        assertNotNull(chatroomRepository.findByEnrollment(enrollment));

        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        Long chatroomId = chatroom.getId();

        // When
        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // Then
        Review review = reviewRepository.findByEnrollment(enrollment);
        assertNotNull(review);
        assertAll(
                () -> assertEquals(enrollment, review.getEnrollment()),
                () -> assertEquals(0, review.getChildren().size()),
                () -> assertEquals(lecture1, review.getLecture()),
                () -> assertEquals(tuteeReviewCreateRequest.getContent(), review.getContent()),
                () -> assertEquals(5, review.getScore())
        );
    }

    @WithAccount("yk")
    @DisplayName("튜티 리뷰 등록 - 종료된 강의")
    @Test
    void createTuteeReview_withClosedLecture() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);

        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());
        assertEquals(1, enrollmentRepository.findByTutee(tutee).size());
        assertEquals(0, cancellationRepository.findByTutee(tutee).size());
        assertNotNull(chatroomRepository.findByEnrollment(enrollment));

        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        Long chatroomId = chatroom.getId();

        // 수강 종료
        enrollmentService.close(tutorUser, lecture1Id, enrollment.getId());

        // When
        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // Then
        Review review = reviewRepository.findByEnrollment(enrollment);
        assertNotNull(review);
        assertAll(
                () -> assertEquals(enrollment, review.getEnrollment()),
                () -> assertEquals(0, review.getChildren().size()),
                () -> assertEquals(lecture1, review.getLecture()),
                () -> assertEquals(tuteeReviewCreateRequest.getContent(), review.getContent()),
                () -> assertEquals(5, review.getScore())
        );
    }

    @WithAccount("yk")
    @DisplayName("튜티 리뷰 등록 - 취소한 강의")
    @Test
    void createTuteeReview_withCanceledLecture() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);

        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());
        assertEquals(1, enrollmentRepository.findByTutee(tutee).size());
        assertEquals(0, cancellationRepository.findByTutee(tutee).size());
        assertNotNull(chatroomRepository.findByEnrollment(enrollment));

        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        Long chatroomId = chatroom.getId();

        enrollmentService.cancel(user, lecture1Id);

        // When
        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // Then
        Review review = reviewRepository.findByEnrollment(enrollment);
        assertNotNull(review);
        assertAll(
                () -> assertEquals(enrollment, review.getEnrollment()),
                () -> assertEquals(0, review.getChildren().size()),
                () -> assertEquals(lecture1, review.getLecture()),
                () -> assertEquals(tuteeReviewCreateRequest.getContent(), review.getContent()),
                () -> assertEquals(5, review.getScore())
        );
    }

    @WithAccount("yk")
    @DisplayName("튜티 리뷰 등록 - 수강 강의가 아닌 경우")
    @Test
    void createTuteeReview_unEnrolled() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);

        // When
        // Then
        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.createTuteeReview(user, lecture2Id, tuteeReviewCreateRequest);
        });
    }

    @WithAccount("yk")
    @DisplayName("튜티 리뷰 수정")
    @Test
    void updateTuteeReview() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());

        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        Review review = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // When
        TuteeReviewUpdateRequest tuteeReviewUpdateRequest = TuteeReviewUpdateRequest.builder()
                .score(3)
                .content("별로에요")
                .build();
        reviewService.updateTuteeReview(user, lecture1Id, review.getId(), tuteeReviewUpdateRequest);

        // Then
        Review updatedReview = reviewRepository.findByEnrollment(enrollment);
        assertNotNull(review);
        assertAll(
                () -> assertEquals(enrollment, updatedReview.getEnrollment()),
                () -> assertEquals(0, updatedReview.getChildren().size()),
                () -> assertEquals(lecture1, updatedReview.getLecture()),
                () -> assertEquals(tuteeReviewUpdateRequest.getContent(), updatedReview.getContent()),
                () -> assertEquals(3, updatedReview.getScore())
        );
    }

    @WithAccount("yk")
    @DisplayName("튜티 리뷰 삭제")
    @Test
    void deleteTuteeReview() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());

        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        Review review = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);
        assertEquals(1, reviewRepository.findByLecture(lecture1).size());

        // When
        reviewService.deleteTuteeReview(user, lecture1Id, review.getId());

        // Then
        assertEquals(0, reviewRepository.findByLecture(lecture1).size());

    }

    @WithAccount("yk")
    @DisplayName("튜터 리뷰 등록")
    @Test
    void createTutorReview() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());

        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        Review parent = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        // When
        TutorReviewCreateRequest tutorReviewCreateRequest = TutorReviewCreateRequest.builder()
                .content("감사합니다!")
                .build();
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

    @WithAccount("yk")
    @DisplayName("튜티 리뷰 삭제 - 튜터가 댓글을 단 경우")
    @Test
    void deleteTuteeReview_withChildren() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());

        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        Review parent = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        TutorReviewCreateRequest tutorReviewCreateRequest = TutorReviewCreateRequest.builder()
                .content("감사합니다!")
                .build();
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

    @WithAccount("yk")
    @DisplayName("튜터 리뷰 수정")
    @Test
    void updateTutorReview() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());

        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        Review parent = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        TutorReviewCreateRequest tutorReviewCreateRequest = TutorReviewCreateRequest.builder()
                .content("감사합니다!")
                .build();
        Review child = reviewService.createTutorReview(tutorUser, lecture1Id, parent.getId(), tutorReviewCreateRequest);

        // When
        TutorReviewUpdateRequest tutorReviewUpdateRequest = TutorReviewUpdateRequest.builder()
                .content("리뷰 감사합니다")
                .build();
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

    @WithAccount("yk")
    @DisplayName("튜터 리뷰 삭제")
    @Test
    void deleteTutorReview() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);

        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        Enrollment enrollment = enrollmentService.enroll(user, lecture1Id, lecturePrice1.getId());

        TuteeReviewCreateRequest tuteeReviewCreateRequest = TuteeReviewCreateRequest.builder()
                .score(5)
                .content("좋아요")
                .build();
        Review parent = reviewService.createTuteeReview(user, lecture1Id, tuteeReviewCreateRequest);

        TutorReviewCreateRequest tutorReviewCreateRequest = TutorReviewCreateRequest.builder()
                .content("감사합니다!")
                .build();
        Review child = reviewService.createTutorReview(tutorUser, lecture1Id, parent.getId(), tutorReviewCreateRequest);

        // When
        reviewService.deleteTutorReview(tutorUser, lecture1Id, parent.getId(), child.getId());

        // Then
        // List<Review> reviews = reviewRepository.findByLecture(lecture1);
        // assertEquals(1, reviews.size());
        assertTrue(reviewRepository.findById(parent.getId()).isPresent());

        // parent = reviewRepository.findById(parent.getId()).orElse(null);
        parent = reviewRepository.findByLecture(lecture1).get(0);
        assertEquals(0, parent.getChildren().size());
        assertFalse(reviewRepository.findById(child.getId()).isPresent());
    }
}