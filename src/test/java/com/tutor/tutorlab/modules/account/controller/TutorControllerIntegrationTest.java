package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@Transactional
@MockMvcTest
class TutorControllerIntegrationTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @WithAccount(NAME)
    @Test
    void newTutor() throws Exception {

        // Given
        // When
        String content = objectMapper.writeValueAsString(tutorSignUpRequest);
        // System.out.println(content);
        mockMvc.perform(post("/tutors")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTOR, user.getRole());
        Tutor tutor = tutorRepository.findByUser(user);
        assertAll(
                () -> assertNotNull(tutor),
                () -> assertEquals(1, careerRepository.findByTutor(tutor).size()),
                () -> assertEquals(1, educationRepository.findByTutor(tutor).size())
        );
    }

    @DisplayName("Tutor 등록 - Invalid Input")
    @WithAccount(NAME)
    @Test
    public void newTutor_withInvalidInput() throws Exception {

        // Given
        // When
        // Then
//        CareerCreateRequest careerCreateRequest = CareerCreateRequest.of(
//                "tutorlab",
//                null,
//                "2007-12-03",
//                "",
//                true
//        );
//
//        tutorSignUpRequest.addCareerCreateRequest(careerCreateRequest);
//        mockMvc.perform(post("/tutors")
//                .content(objectMapper.writeValueAsString(tutorSignUpRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$.message").value("Invalid Input"))
//                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("Tutor 등록 - 인증된 사용자 X")
    public void newTutor_withoutAuthenticatedUser() throws Exception {

        // Given
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        // Then
        mockMvc.perform(post("/tutors")
                .content(objectMapper.writeValueAsString(tutorSignUpRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @WithAccount(NAME)
    @Test
    void Tutor_수정() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        mockMvc.perform(put("/tutors")
                .content(objectMapper.writeValueAsString(tutorUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTOR, user.getRole());

        Tutor tutor = tutorRepository.findByUser(user);
        // TODO - career, education 확인
    }

    // TODO - Tutor 삭제 시 연관 엔티티 전체 삭제
    @WithAccount(NAME)
    @Test
    void Tutor_탈퇴() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());

        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        List<Long> careerIds = careerRepository.findByTutor(tutor).stream()
                .map(career -> career.getId()).collect(Collectors.toList());
        List<Long> educationIds = educationRepository.findByTutor(tutor).stream()
                .map(education -> education.getId()).collect(Collectors.toList());

        // When
        mockMvc.perform(delete("/tutors"))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());

        // tutor
        assertNull(tutorRepository.findByUser(user));
        // career
        for (Long careerId : careerIds) {
            assertFalse(careerRepository.findById(careerId).isPresent());
        }
        // education
        for (Long educationId : educationIds) {
            assertFalse(educationRepository.findById(educationId).isPresent());
        }
        // chatroom
        // message
        // lecture - lecturePrice, lectureSubject
        // enrollment, pick, review


    }

    // TODO - Tutor 삭제 시 연관 엔티티 전체 삭제
    @WithAccount(NAME)
    @Test
    @DisplayName("Tutor 탈퇴 - 튜터가 아닌 경우")
    public void quitTutor_notTutor() throws Exception {

        // Given

        // When

        // Then
    }

}