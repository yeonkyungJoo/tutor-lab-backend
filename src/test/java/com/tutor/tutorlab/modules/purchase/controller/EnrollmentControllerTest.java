package com.tutor.tutorlab.modules.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
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
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@MockMvcTest
class EnrollmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EnrollmentRepository enrollmentRepository;

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

    @Autowired
    ObjectMapper objectMapper;

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
    void 강의수강() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        // When
        mockMvc.perform(post("/lectures/{lecture_id}/enrollments", lectureId))
                .andDo(print())
                .andExpect(status().isCreated());

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
}