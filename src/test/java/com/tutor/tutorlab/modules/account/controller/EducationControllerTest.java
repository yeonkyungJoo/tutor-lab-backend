package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.EducationService;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
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
class EducationControllerTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


//    @Test
//    void getEducation() {
//    }

    @WithAccount(NAME)
    @Test
    void Education_등록() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        mockMvc.perform(post("/educations")
                .content(objectMapper.writeValueAsString(educationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(1, educationRepository.findByTutor(tutor).size());

        Education createdEducation = educationRepository.findByTutor(tutor).get(0);
        assertAll(
                () -> assertEquals(educationCreateRequest.getSchoolName(), createdEducation.getSchoolName()),
                () -> assertEquals(educationCreateRequest.getMajor(), createdEducation.getMajor()),
                () -> assertEquals(LocalDate.parse(educationCreateRequest.getEntranceDate()), createdEducation.getEntranceDate()),
                () -> assertEquals(LocalDate.parse(educationCreateRequest.getGraduationDate()), createdEducation.getGraduationDate()),
                () -> assertEquals(educationCreateRequest.getScore(), createdEducation.getScore()),
                () -> assertEquals(educationCreateRequest.getDegree(), createdEducation.getDegree())
        );
    }

    @WithAccount(NAME)
    @Test
    void Education_등록_withInvalidInput() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        // Then - Invalid Input
        educationCreateRequest.setEntranceDate("2021-04-01");

        mockMvc.perform(post("/educations")
                .content(objectMapper.writeValueAsString(educationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Invalid Input"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void Education_등록_withoutAuthenticatedUser() throws Exception {

        // Given
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        // Then
        mockMvc.perform(post("/educations")
                .content(objectMapper.writeValueAsString(educationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @WithAccount(NAME)
    @Test
    void Education_등록_notTutor() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());

        // When
        // Then
        mockMvc.perform(post("/educations")
                .content(objectMapper.writeValueAsString(educationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @WithAccount(NAME)
    @Test
    void Education_수정() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        Education education = educationService.createEducation(user, educationCreateRequest);
        Long educationId = education.getId();

        mockMvc.perform(put("/educations/" + educationId)
                .content(objectMapper.writeValueAsString(educationUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(1, educationRepository.findByTutor(tutor).size());

        Education updatedEducation = educationRepository.findByTutor(tutor).get(0);
        assertAll(
                () -> assertEquals(educationUpdateRequest.getSchoolName(), updatedEducation.getSchoolName()),
                () -> assertEquals(educationUpdateRequest.getMajor(), updatedEducation.getMajor()),
                () -> assertEquals(LocalDate.parse(educationUpdateRequest.getEntranceDate()), updatedEducation.getEntranceDate()),
                () -> assertEquals(LocalDate.parse(educationUpdateRequest.getGraduationDate()), updatedEducation.getGraduationDate()),
                () -> assertEquals(educationUpdateRequest.getScore(), updatedEducation.getScore()),
                () -> assertEquals(educationUpdateRequest.getDegree(), updatedEducation.getDegree())
        );
    }

    @WithAccount(NAME)
    @Test
    void Education_삭제() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        Education education = educationService.createEducation(user, educationCreateRequest);
        Long educationId = education.getId();

        // When
        mockMvc.perform(delete("/educations/" + educationId))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);

        List<Education> educations = educationRepository.findByTutor(tutor);
        assertEquals(0, educations.size());
        assertFalse(educationRepository.findById(educationId).isPresent());
    }
}