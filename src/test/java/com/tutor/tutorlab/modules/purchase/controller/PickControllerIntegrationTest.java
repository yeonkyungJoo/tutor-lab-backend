package com.tutor.tutorlab.modules.purchase.controller;

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
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.LearningKindType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@Transactional
@MockMvcTest
class PickControllerIntegrationTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;

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
    @Test
    void addPick() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(user);

        // When
        mockMvc.perform(post("/lectures/{lecture_id}/picks", lecture1Id))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        List<Pick> picks = pickRepository.findByTutee(tutee);
        assertEquals(1, picks.size());
        Pick pick = picks.get(0);
        assertAll(
                () -> assertNotNull(pick),
                () -> assertEquals(tutee, pick.getTutee()),
                () -> assertEquals(lecture1, pick.getLecture())
        );
    }
}