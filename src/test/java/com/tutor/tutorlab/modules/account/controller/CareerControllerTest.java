package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.CareerService;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TutorService;
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
class CareerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    TutorService tutorService;
    @Autowired
    TuteeRepository tuteeRepository;
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    CareerService careerService;
    @Autowired
    LoginService loginService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithAccount("yk")
    void newCareer() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        // Then
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();

        mockMvc.perform(post("/careers")
                .content(objectMapper.writeValueAsString(careerCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(1, careerRepository.findByTutor(tutor).size());

        Career career = careerRepository.findByTutor(tutor).get(0);
        assertFalse(career.isPresent());
        assertEquals(career.getDuty(), "engineer");
        assertEquals(career.getCompanyName(), "tutorlab");
    }

    @Test
    @DisplayName("Career 등록 - Invalid Input")
    @WithAccount("yk")
    void newCareer_withInvalidInput() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        // Then
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("")
                .present(false)
                .build();

        mockMvc.perform(post("/careers")
                .content(objectMapper.writeValueAsString(careerCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Invalid Input"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("Career 등록 - 인증된 사용자 X")
    public void newCareer_withoutAuthenticatedUser() throws Exception {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
                .gender("MALE")
                .phoneNumber(null)
                .email(null)
                .nickname("test")
                .bio(null)
                .zone("서울시 강남구 역삼동")
                .build();
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        user = userRepository.findByUsername("test@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        // Then
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();

        mockMvc.perform(post("/careers")
                .content(objectMapper.writeValueAsString(careerCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @WithAccount("yk")
    @Test
    @DisplayName("Career 등록 - 튜터가 아닌 경우")
    public void newCareer_notTutor() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());

        // When
        // Then
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();

        mockMvc.perform(post("/careers")
                .content(objectMapper.writeValueAsString(careerCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
        // .andExpect(jsonPath("$.message").value("해당 사용자는 " + RoleType.TUTOR.getName() + "가 아닙니다."));
    }

    @WithAccount("yk")
    @Test
    void Career_수정() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        Career career = careerService.createCareer(user, careerCreateRequest);
        Long careerId = career.getId();

        // When
        CareerUpdateRequest careerUpdateRequest = CareerUpdateRequest.builder()
                .companyName("tutorlab2")
                .duty("engineer")
                .startDate("2007-12-03")
                //.endDate("")
                .present(true)
                .build();

        mockMvc.perform(put("/careers/" + careerId)
                .content(objectMapper.writeValueAsString(careerUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        List<Career> careers = careerRepository.findByTutor(tutor);
        assertEquals(1, careers.size());
        career = careers.get(0);
        assertEquals("tutorlab2", career.getCompanyName());
        assertEquals("engineer", career.getDuty());
        assertEquals(LocalDate.parse("2007-12-03"), career.getStartDate());
        assertNull(career.getEndDate());
        assertTrue(career.isPresent());
    }

    @WithAccount("yk")
    @Test
    void Career_삭제() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        Career career = careerService.createCareer(user, careerCreateRequest);
        Long careerId = career.getId();

        // When
        mockMvc.perform(delete("/careers/" + careerId))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        List<Career> careers = careerRepository.findByTutor(tutor);
        assertEquals(0, careers.size());

        assertFalse(careerRepository.findById(careerId).isPresent());
    }
}