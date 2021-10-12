package com.tutor.tutorlab.modules.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@MockMvcTest
class EnrollmentControllerTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

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
    void 강의수강() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        LecturePrice lecturePrice = lecturePriceRepository.findByLecture(lecture).get(0);
        Long lecturePriceId = lecturePrice.getId();

        // When
        mockMvc.perform(post("/lectures/{lecture_id}/{lecture_price_id}/enrollments", lectureId, lecturePriceId))
                .andDo(print())
                .andExpect(status().isCreated());

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
        List<Chatroom> chatrooms = chatroomRepository.findByTutorAndTutee(tutor, tutee);
        assertAll(
                () -> assertNotNull(chatroom),
                () -> assertEquals(1, chatrooms.size()),
                () -> assertEquals(chatroom, chatrooms.get(0)),
                () -> assertEquals(enrollment, chatroom.getEnrollment()),
                () -> assertEquals(enrollment.getLecture().getTutor(), chatroom.getTutor()),
                () -> assertEquals(tutee, chatroom.getTutee())
        );
    }
}