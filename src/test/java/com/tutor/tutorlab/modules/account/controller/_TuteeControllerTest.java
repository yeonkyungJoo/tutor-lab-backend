package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
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
public class _TuteeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TuteeRepository tuteeRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    // @Transactional
    public void init() throws Exception {

    }

    @Test
    @DisplayName("Tutee 수정 / 추가 정보 입력")
    @WithAccount("yk")
    public void editTutee() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        assertEquals(RoleType.TUTEE, user.getRole());

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

    // TODO
    @Test
    @DisplayName("Tutee 수정 - 인증된 사용자 X")
    public void editTutee_withoutAuthenticatedUser() throws Exception {

        // Given

        // When

        // Then
    }

    @Test
    @DisplayName("Tutee 삭제")
    @WithAccount("yk")
    public void removeTutee() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        assertEquals(RoleType.TUTEE, user.getRole());
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);

        // When
        mockMvc.perform(delete("/tutees"))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        assertNull(tuteeRepository.findByUser(user));
        assertNull(userRepository.findByName("yk"));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}