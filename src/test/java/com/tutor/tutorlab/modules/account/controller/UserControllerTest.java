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
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @WithAccount("yk")
    @Test
    void 회원정보_수정() throws Exception {

        // Given
        // When
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .phoneNumber("010-1234-5678")
                .email("yk@email.com")
                .nickname("nickname")
                .zone("서울시 서초구")
                .build();

        mockMvc.perform(put("/users")
                .content(objectMapper.writeValueAsString(userUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertNotNull(user);
        assertEquals("010-1234-5678", user.getPhoneNumber());
        assertEquals("yk@email.com", user.getEmail());
        assertEquals("nickname", user.getNickname());
        assertEquals(null, user.getBio());
        assertEquals("서울시 서초구", user.getZone());
    }

    // TODO - 회원 삭제 시 연관 엔티티 전체 삭제
    @WithAccount("yk")
    @Test
    void 회원탈퇴() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
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
        user = userRepository.findAllByUsername("yk@email.com");
        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
        assertEquals(RoleType.TUTEE, user.getRole());
        // 튜티
        assertNull(tuteeRepository.findByUser(user));
        // 튜터
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
        // notification
        // file
    }
}