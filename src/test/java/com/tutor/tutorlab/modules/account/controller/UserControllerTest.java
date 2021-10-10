package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.*;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@MockMvcTest
class UserControllerTest extends AbstractTest {

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

    @WithAccount(NAME)
    @Test
    void 회원정보_수정() throws Exception {

        // Given
        // When
        mockMvc.perform(put("/users")
                .content(objectMapper.writeValueAsString(userUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals(userUpdateRequest.getPhoneNumber(), user.getPhoneNumber()),
                () -> assertEquals(userUpdateRequest.getEmail(), user.getEmail()),
                () -> assertEquals(userUpdateRequest.getNickname(), user.getNickname()),
                () -> assertEquals(userUpdateRequest.getBio(), user.getBio()),
                () -> assertEquals(userUpdateRequest.getZone(), user.getZone().toString())
        );
    }

    // TODO - 회원 삭제 시 연관 엔티티 전체 삭제
    @WithAccount(NAME)
    @Test
    void 회원탈퇴() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);

        tutorSignUpRequest = AbstractTest.getTutorSignUpRequest(true);
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        List<Long> careerIds = careerRepository.findByTutor(tutor).stream()
                .map(career -> career.getId()).collect(Collectors.toList());
        List<Long> educationIds = educationRepository.findByTutor(tutor).stream()
                .map(education -> education.getId()).collect(Collectors.toList());

        // When
        mockMvc.perform(delete("/users"))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        // 세션
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // 유저
        User deletedUser = userRepository.findAllByUsername(USERNAME);
        assertTrue(deletedUser.isDeleted());
        assertNotNull(deletedUser.getDeletedAt());
        assertEquals(RoleType.TUTEE, deletedUser.getRole());

        // 튜티
        assertNull(tuteeRepository.findByUser(deletedUser));
        // 튜터
        assertNull(tutorRepository.findByUser(deletedUser));
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
        // notification
        // file
    }
}