package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.config.exception.AlreadyExistException;
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
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
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
class EnrollmentServiceTest {

//    @Test
//    void getLecturesOfTutee() {
//    }
//
//    @Test
//    void getLectureResponsesOfTutee() {
//    }

    @Autowired
    EnrollmentService enrollmentService;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    CancellationRepository cancellationRepository;

    @Autowired
    LoginService loginService;
    @Autowired
    TutorService tutorService;

    @Autowired
    LectureService lectureService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TuteeRepository tuteeRepository;

    @Autowired
    ChatroomRepository chatroomRepository;

    private Tutor tutor;
    private Lecture lecture;
    private Long lectureId;

    @BeforeEach
    void init() {

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk2@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk2")
                .gender("FEMALE")
                .build();
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

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
        tutor = tutorService.createTutor(user, tutorSignUpRequest);

        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest1
                = LectureBuilder.getLecturePriceCreateRequest(true, 3, 1000L, 3, 3000L, 10);
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest1
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바");

        LectureCreateRequest lectureCreateRequest = LectureCreateRequest.builder()
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
        lecture = lectureService.createLecture(user, lectureCreateRequest);
        lectureId = lecture.getId();
    }

    @WithAccount("yk")
    @Test
    void 강의수강() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        // When
        enrollmentService.enroll(user, lectureId);

        // Then
        assertEquals(1, enrollmentRepository.findByTutee(tutee).size());
        Enrollment enrollment = enrollmentRepository.findByTutee(tutee).get(0);
        assertNotNull(enrollment);
        assertEquals("제목", enrollment.getLecture().getTitle());
        assertEquals("java,spring", enrollment.getLecture().getTutor().getSubjects());
        assertFalse(enrollment.getLecture().getTutor().isSpecialist());
        assertEquals("yk", enrollment.getTutee().getUser().getName());

        // 강의 수강 시 채팅방 자동 생성
        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        assertNotNull(chatroom);
        List<Chatroom> chatrooms = chatroomRepository.findByTutorAndTutee(tutor, tutee);
        assertEquals(1, chatrooms.size());
        assertEquals(chatroom, chatrooms.get(0));
        assertEquals(enrollment, chatroom.getEnrollment());
        assertEquals(enrollment.getLecture().getTutor(), chatroom.getTutor());
        assertEquals(tutee, chatroom.getTutee());
    }

    @WithAccount("yk")
    @Test
    void 강의중복수강_실패() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        enrollmentService.enroll(user, lectureId);

        // When
        assertThrows(AlreadyExistException.class, () -> {
            enrollmentService.enroll(user, lectureId);
        });

    }

    @DisplayName("강의 구매 취소")
    @WithAccount("yk")
    @Test
    void cancel() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        Enrollment enrollment = enrollmentService.enroll(user, lectureId);
        assertEquals(1, enrollmentRepository.findByTutee(tutee).size());
        assertEquals(0, cancellationRepository.findByTutee(tutee).size());
        assertNotNull(chatroomRepository.findByEnrollment(enrollment));

        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        Long chatroomId = chatroom.getId();
        // When
        enrollmentService.cancel(user, lectureId);

        // Then
        List<Enrollment> enrollments = enrollmentRepository.findByTutee(tutee);
        assertEquals(0, enrollments.size());

        List<Cancellation> cancellations = cancellationRepository.findByTutee(tutee);
        assertEquals(1, cancellations.size());
        Cancellation cancellation = cancellations.get(0);
        assertNotNull(cancellation);
        assertEquals("제목", cancellation.getLecture().getTitle());
        assertEquals("yk", cancellation.getTutee().getUser().getName());

        assertFalse(chatroomRepository.findById(chatroomId).isPresent());
        List<Chatroom> chatrooms = chatroomRepository.findByTutorAndTutee(tutor, tutee);
        assertEquals(0, chatrooms.size());
    }

    @DisplayName("강의 종료")
    @WithAccount("yk")
    @Test
    void close() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        Enrollment enrollment = enrollmentService.enroll(user, lectureId);
        Chatroom chatroom = chatroomRepository.findByEnrollment(enrollment).orElse(null);
        assertNotNull(chatroom);
        assertFalse(enrollment.isClosed());

        Long enrollmentId = enrollment.getId();
        Long chatroomId = chatroom.getId();
        User tutorUser = userRepository.findByUsername("yk2@email.com").orElse(null);

        // When
        enrollmentService.close(tutorUser, lectureId, enrollmentId);

        // Then
//        enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
//        assertTrue(enrollment.isClosed());
        enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture).orElse(null);
        assertNull(enrollment);
        enrollment = enrollmentRepository.findAllById(enrollmentId);
        assertNotNull(enrollment);
        assertTrue(enrollment.isClosed());

        assertFalse(chatroomRepository.findById(chatroomId).isPresent());
        List<Chatroom> chatrooms = chatroomRepository.findByTutorAndTutee(tutor, tutee);
        assertEquals(0, chatrooms.size());
        assertFalse(chatroomRepository.findByEnrollment(enrollment).isPresent());
    }
}