package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@MockMvcTest
class CareerControllerIntegrationTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithAccount(NAME)
    void newCareer() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        mockMvc.perform(post("/careers")
                .content(objectMapper.writeValueAsString(careerCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);

        Assertions.assertEquals(1, careerRepository.findByTutor(tutor).size());
        Career createdCareer = careerRepository.findByTutor(tutor).get(0);
        assertAll(
                () -> assertEquals(careerCreateRequest.getCompanyName(), createdCareer.getCompanyName())
        );
    }

    @Test
    @DisplayName("Career 등록 - Invalid Input")
    @WithAccount(NAME)
    void newCareer_withInvalidInput() throws Exception {

//        // Given
//        User user = userRepository.findByUsername(USERNAME).orElse(null);
//        tutorService.createTutor(user, tutorSignUpRequest);
//
//        // When
//        // Then - Invalid Input
//
//        mockMvc.perform(post("/careers")
//                .content(objectMapper.writeValueAsString(careerCreateRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$.message").value("Invalid Input"))
//                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("Career 등록 - 인증된 사용자 X")
    public void newCareer_withoutAuthenticatedUser() throws Exception {

        // Given
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        // Then
        mockMvc.perform(post("/careers")
                .content(objectMapper.writeValueAsString(careerCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @WithAccount(NAME)
    @Test
    @DisplayName("Career 등록 - 튜터가 아닌 경우")
    public void newCareer_notTutor() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());

        // When
        // Then
        mockMvc.perform(post("/careers")
                .content(objectMapper.writeValueAsString(careerCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
        // .andExpect(jsonPath("$.message").value("해당 사용자는 " + RoleType.TUTOR.getName() + "가 아닙니다."));
    }

    @WithAccount(NAME)
    @Test
    void Career_수정() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        Career career = careerService.createCareer(user, careerCreateRequest);
        Long careerId = career.getId();

        // When
        mockMvc.perform(put("/careers/" + careerId)
                .content(objectMapper.writeValueAsString(careerUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);

        List<Career> careers = careerRepository.findByTutor(tutor);
        assertEquals(1, careers.size());

        Career updatedCareer = careers.get(0);
        assertAll(
                () -> assertEquals(careerUpdateRequest.getCompanyName(), updatedCareer.getCompanyName())
        );
    }

    @WithAccount(NAME)
    @Test
    void Career_삭제() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        Career career = careerService.createCareer(user, careerCreateRequest);
        Long careerId = career.getId();

        // When
        mockMvc.perform(delete("/careers/" + careerId))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        List<Career> careers = careerRepository.findByTutor(tutor);
        assertEquals(0, careers.size());

        assertFalse(careerRepository.findById(careerId).isPresent());
    }
}