package com.tutor.tutorlab.modules.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@Transactional
@MockMvcTest
class EnrollmentControllerIntegrationTest extends AbstractTest {

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
    void ????????????() throws Exception {

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
        assertEquals(1, enrollmentRepository.findByTuteeAndCanceledFalseAndClosedFalse(tutee).size());
        Enrollment enrollment = enrollmentRepository.findByTuteeAndCanceledFalseAndClosedFalse(tutee).get(0);
        assertAll(
                () -> assertNotNull(enrollment),
                () -> assertEquals(lecture.getTitle(), enrollment.getLecture().getTitle()),
                () -> assertEquals(tutee.getUser().getName(), enrollment.getTutee().getUser().getName()),
                () -> assertEquals(lecturePrice.getIsGroup(), enrollment.getLecturePrice().getIsGroup()),
                () -> assertEquals(lecturePrice.getGroupNumber(), enrollment.getLecturePrice().getGroupNumber()),
                () -> assertEquals(lecturePrice.getPertimeCost(), enrollment.getLecturePrice().getPertimeCost()),
                () -> assertEquals(lecturePrice.getPertimeLecture(), enrollment.getLecturePrice().getPertimeLecture())
        );

        // ?????? ?????? ??? ????????? ?????? ??????
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