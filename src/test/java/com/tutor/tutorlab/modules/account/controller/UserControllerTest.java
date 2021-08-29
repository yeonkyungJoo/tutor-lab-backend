package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.*;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorService tutorService;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    TuteeRepository tuteeRepository;
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    EducationRepository educationRepository;

    @Autowired
    ObjectMapper objectMapper;

    // TODO - 권한 에러

    @Test
    @DisplayName("회원 정보 수정")
    @WithAccount("yk")
    public void editUser() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        user.setEmail("010-1234-5678");
        user.setBio("bio");

        // When
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .phoneNumber("010-1234-5678")
                .email("yk@email.com")
                .nickname("nickname")
                //.bio(null)
                .zone("서울시 서초구")
                .build();

        mockMvc.perform(put("/users")
                .content(objectMapper.writeValueAsString(userUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByName("yk");
        assertEquals("010-1234-5678", user.getPhoneNumber());
        assertEquals("yk@email.com", user.getEmail());
        assertEquals("nickname", user.getNickname());
        assertEquals(null, user.getBio());
        assertEquals("서울시 서초구", user.getZone());
    }

    @Test
    @DisplayName("회원 탈퇴")
    @WithAccount("yk")
    public void quitUser() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        Tutee tutee = tuteeRepository.findByUser(user);
        Long userId = user.getId();
        Long tuteeId = tutee.getId();

        // 튜터인 경우
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();

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

        tutorSignUpRequest.addCareerCreateRequest(careerCreateRequest);
        tutorSignUpRequest.addEducationCreateRequest(educationCreateRequest);

        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        Career career = tutor.getCareers().get(0);
        assertNotNull(career);
        Education education = tutor.getEducations().get(0);
        assertNotNull(education);

        Long tutorId = tutor.getId();
        Long careerId = career.getId();
        Long educationId = education.getId();
        assertEquals(RoleType.ROLE_TUTOR, user.getRole());

        // When
        mockMvc.perform(delete("/users"))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        assertFalse(userRepository.findById(userId).isPresent());
        assertFalse(tuteeRepository.findById(tuteeId).isPresent());
        assertFalse(tutorRepository.findById(tutorId).isPresent());
        assertFalse(careerRepository.findById(careerId).isPresent());
        assertFalse(educationRepository.findById(educationId).isPresent());

        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }

}