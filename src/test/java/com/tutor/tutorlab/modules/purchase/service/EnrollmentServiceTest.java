package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class EnrollmentServiceTest extends AbstractTest {

    private Tutor tutor;
    private Lecture lecture;
    private Long lectureId;

    @BeforeEach
    void init() {

        SignUpRequest signUpRequest = getSignUpRequest("tutor", "tutor");
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        tutor = tutorService.createTutor(user, tutorSignUpRequest);
        lecture = lectureService.createLecture(user, lectureCreateRequest);
        lectureId = lecture.getId();
    }

    @WithAccount(NAME)
    @Test
    void 강의수강() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice = lecturePriceRepository.findByLecture(lecture).get(0);
        Long lecturePriceId = lecturePrice.getId();

        // When
        enrollmentService.createEnrollment(user, lectureId, lecturePriceId);

        // Then
        assertEquals(1, enrollmentRepository.findByTutee(tutee).size());
        Enrollment enrollment = enrollmentRepository.findByTutee(tutee).get(0);
        assertAll(
                () -> assertNotNull(enrollment),
                () -> assertEquals(lecture.getTitle(), enrollment.getLecture().getTitle()),
                () -> assertEquals(tutor.getSubjects(), enrollment.getLecture().getTutor().getSubjects()),
                () -> assertEquals(tutor.isSpecialist(), enrollment.getLecture().getTutor().isSpecialist()),
                () -> assertEquals(tutee.getUser().getName(), enrollment.getTutee().getUser().getName()),
                () -> assertEquals(lecturePrice.getIsGroup(), enrollment.getLecturePrice().getIsGroup()),
                () -> assertEquals(lecturePrice.getGroupNumber(), enrollment.getLecturePrice().getGroupNumber()),
                () -> assertEquals(lecturePrice.getPertimeCost(), enrollment.getLecturePrice().getPertimeCost()),
                () -> assertEquals(lecturePrice.getPertimeLecture(), enrollment.getLecturePrice().getPertimeLecture())
        );

        // 강의 수강 시 채팅방 자동 생성
        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        assertNotNull(chatroom);
        List<Chatroom> chatrooms = chatroomRepository.findByTutorAndTutee(tutor, tutee);
        assertAll(
                () -> assertEquals(1, chatrooms.size()),
                () -> assertEquals(chatroom, chatrooms.get(0)),
                () -> assertEquals(enrollment, chatroom.getEnrollment()),
                () -> assertEquals(enrollment.getLecture().getTutor(), chatroom.getTutor()),
                () -> assertEquals(tutee, chatroom.getTutee())
        );
    }

    @WithAccount(NAME)
    @Test
    void 강의중복수강_실패() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice = lecturePriceRepository.findByLecture(lecture).get(0);
        Long lecturePriceId = lecturePrice.getId();

        enrollmentService.createEnrollment(user, lectureId, lecturePriceId);

        // When
        assertThrows(AlreadyExistException.class, () -> {
            enrollmentService.createEnrollment(user, lectureId, lecturePriceId);
        });

    }

    @DisplayName("강의 구매 취소")
    @WithAccount(NAME)
    @Test
    void cancel() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice = lecturePriceRepository.findByLecture(lecture).get(0);
        Long lecturePriceId = lecturePrice.getId();

        Enrollment enrollment = enrollmentService.createEnrollment(user, lectureId, lecturePriceId);
        assertAll(
                () -> assertFalse(enrollment.isCanceled()),
                () -> assertEquals(1, enrollmentRepository.findByTutee(tutee).size())
        );

        assertNotNull(chatroomRepository.findByEnrollment(enrollment));
        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        Long chatroomId = chatroom.getId();

        // When
        enrollmentService.cancel(user, lectureId);

        // Then
        assertEquals(0, enrollmentRepository.findByTutee(tutee).size());
        assertEquals(1, enrollmentRepository.findAllByTutee(tutee.getId()).size());
        assertTrue(enrollment.isCanceled());

        Cancellation cancellation = cancellationRepository.findByEnrollment(enrollment);
        assertAll(
                () -> assertNotNull(cancellation),
                () -> assertEquals(lecture.getTitle(), enrollment.getLecture().getTitle()),
                () -> assertEquals(tutee.getUser().getName(), enrollment.getTutee().getUser().getName())
        );

        assertFalse(chatroomRepository.findById(chatroomId).isPresent());
        List<Chatroom> chatrooms = chatroomRepository.findByTutorAndTutee(tutor, tutee);
        assertEquals(0, chatrooms.size());
    }

    @DisplayName("강의 종료")
    @WithAccount(NAME)
    @Test
    void close() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice = lecturePriceRepository.findByLecture(lecture).get(0);
        Long lecturePriceId = lecturePrice.getId();

        Enrollment enrollment = enrollmentService.createEnrollment(user, lectureId, lecturePriceId);
        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        assertNotNull(chatroom);

        assertFalse(enrollment.isCanceled());
        assertFalse(enrollment.isClosed());

        Long enrollmentId = enrollment.getId();
        Long chatroomId = chatroom.getId();
        User tutorUser = tutor.getUser();

        // When
        enrollmentService.close(tutorUser, lectureId, enrollmentId);

        // Then
//        enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
//        assertTrue(enrollment.isClosed());
        enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture).orElse(null);
        assertNull(enrollment);
        enrollment = enrollmentRepository.findAllById(enrollmentId);
        assertNotNull(enrollment);
        assertFalse(enrollment.isCanceled());
        assertTrue(enrollment.isClosed());

        assertFalse(chatroomRepository.findById(chatroomId).isPresent());
        List<Chatroom> chatrooms = chatroomRepository.findByTutorAndTutee(tutor, tutee);
        assertEquals(0, chatrooms.size());
        assertFalse(chatroomRepository.findByEnrollment(enrollment).isPresent());
    }
}