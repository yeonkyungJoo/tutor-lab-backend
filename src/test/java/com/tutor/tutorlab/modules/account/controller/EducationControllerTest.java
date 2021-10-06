package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.config.init.TestDataBuilder;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
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
class EducationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EducationService educationService;
    @Autowired
    EducationRepository educationRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    TutorService tutorService;
    @Autowired
    TuteeRepository tuteeRepository;
    @Autowired
    LoginService loginService;

    @Autowired
    ObjectMapper objectMapper;

    private TutorSignUpRequest tutorSignUpRequest = TestDataBuilder.getTutorSignUpRequest("java,spring");
    private EducationCreateRequest educationCreateRequest = TestDataBuilder.getEducationCreateRequest("school", "computer");

//    @Test
//    void getEducation() {
//    }

    @WithAccount("yk")
    @Test
    void Education_등록() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        mockMvc.perform(post("/educations")
                .content(objectMapper.writeValueAsString(educationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(1, educationRepository.findByTutor(tutor).size());

        Education education = educationRepository.findByTutor(tutor).get(0);
        assertAll(
                () -> assertEquals("school", education.getSchoolName()),
                () -> assertEquals("computer", education.getMajor()),
                () -> assertEquals(LocalDate.parse("2021-01-01"), education.getEntranceDate()),
                () -> assertEquals(LocalDate.parse("2021-02-01"), education.getGraduationDate()),
                () -> assertEquals(4.01, education.getScore()),
                () -> assertEquals("Bachelor", education.getDegree())
        );
    }

    @WithAccount("yk")
    @Test
    void Education_등록_withInvalidInput() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
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
        SignUpRequest signUpRequest = TestDataBuilder.getSignUpRequest("yk", "서울특별시 강남구 삼성동");
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        user = userRepository.findByUsername("yk@email.com").orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        mockMvc.perform(post("/educations")
                .content(objectMapper.writeValueAsString(educationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @WithAccount("yk")
    @Test
    void Education_등록_notTutor() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());

        // When
        mockMvc.perform(post("/educations")
                .content(objectMapper.writeValueAsString(educationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @WithAccount("yk")
    @Test
    void Education_수정() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        Education education = educationService.createEducation(user, educationCreateRequest);
        Long educationId = education.getId();

        // Then
        EducationUpdateRequest educationUpdateRequest = TestDataBuilder.getEducationUpdateRequest("school", "computer science", "2021-01-01", "2021-09-01", 4.10, "Master");
        mockMvc.perform(put("/educations/" + educationId)
                .content(objectMapper.writeValueAsString(educationUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(1, educationRepository.findByTutor(tutor).size());

        education = educationRepository.findByTutor(tutor).get(0);

        assertEquals("school", education.getSchoolName());
        assertEquals("computer science", education.getMajor());
        assertEquals(LocalDate.parse("2021-01-01"), education.getEntranceDate());
        assertEquals(LocalDate.parse("2021-09-01"), education.getGraduationDate());
        assertEquals(4.10, education.getScore());
        assertEquals("Master", education.getDegree());
    }

    @WithAccount("yk")
    @Test
    void Education_삭제() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        Education education = educationService.createEducation(user, educationCreateRequest);
        Long educationId = education.getId();

        // When
        mockMvc.perform(delete("/educations/" + educationId))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);

        List<Education> educations = educationRepository.findByTutor(tutor);
        assertEquals(0, educations.size());
        assertFalse(educationRepository.findById(educationId).isPresent());
    }
}