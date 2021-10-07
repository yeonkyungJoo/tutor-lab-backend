package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.configuration.AbstractTest;
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
class TuteeControllerTest extends AbstractTest {

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

    @WithAccount(NAME)
    @Test
    void Tutee_수정() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);
        assertEquals(0, tutee.getSubjectList().size());

        // When
        mockMvc.perform(put("/tutees")
                .content(objectMapper.writeValueAsString(tuteeUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        Tutee updatedTutee = tuteeRepository.findByUser(user);
        assertAll(
                () -> assertEquals(2, updatedTutee.getSubjectList().size()),
                () -> assertTrue(updatedTutee.getSubjects().contains("spring"))
        );
    }

    @Test
    @DisplayName("Tutee 수정 - 인증된 사용자 X")
    public void editTutee_withoutAuthenticatedUser() throws Exception {

        // Given
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        // Then
        mockMvc.perform(put("/tutees")
                .content(objectMapper.writeValueAsString(tuteeUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    // TODO - Tutee 삭제 시 연관 엔티티 전체 삭제
    @WithAccount(NAME)
    @Test
    void Tutee_탈퇴() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);

        // When
        // Then
        // 세션
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // 유저
        User deletedUser = userRepository.findAllByUsername(USERNAME);
        assertAll(
                () -> assertTrue(deletedUser.isDeleted()),
                () -> assertNotNull(deletedUser.getDeletedAt()),
                () -> assertEquals(RoleType.TUTEE, deletedUser.getRole())
        );

        // 튜티
        assertNull(tuteeRepository.findByUser(deletedUser));
        // chatroom
        // message
        // lecture - lecturePrice, lectureSubject
        // enrollment, pick, review
        // notification
        // file

    }
}