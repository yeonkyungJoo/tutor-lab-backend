package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TuteeService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@MockMvcTest
class TuteeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TuteeService tuteeService;
    @Autowired
    TuteeRepository tuteeRepository;
    @Autowired
    LoginService loginService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @WithAccount("yk")
    @Test
    void Tutee_수정() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);
        assertEquals(0, tutee.getSubjectList().size());

        // When
        TuteeUpdateRequest tuteeUpdateRequest = TuteeUpdateRequest.builder()
                .subjects("java,spring")
                .build();

        mockMvc.perform(put("/tutees")
                .content(objectMapper.writeValueAsString(tuteeUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        tutee = tuteeRepository.findByUser(user);
        assertEquals(2, tutee.getSubjectList().size());
        assertTrue(tutee.getSubjects().contains("spring"));
    }

    @Test
    @DisplayName("Tutee 수정 - 인증된 사용자 X")
    public void editTutee_withoutAuthenticatedUser() throws Exception {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk")
                .gender("FEMALE")
                .build();
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        // Then
        TuteeUpdateRequest tuteeUpdateRequest = TuteeUpdateRequest.builder()
                .subjects("java,spring")
                .build();

        mockMvc.perform(put("/tutees")
                .content(objectMapper.writeValueAsString(tuteeUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    // TODO - Tutee 삭제 시 연관 엔티티 전체 삭제
    @WithAccount("yk")
    @Test
    void Tutee_탈퇴() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);

        // When
        TuteeUpdateRequest tuteeUpdateRequest = TuteeUpdateRequest.builder()
                .subjects("java,spring")
                .build();

        // Then
        // 세션
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // 유저
        user = userRepository.findAllByUsername("yk@email.com");
        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
        assertEquals(RoleType.TUTEE, user.getRole());
        // 튜티
        assertNull(tuteeRepository.findByUser(user));
        // chatroom
        // message
        // lecture - lecturePrice, lectureSubject
        // enrollment, pick, review
        // notification
        // file

    }
}