package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class CareerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    // @Transactional
    public void init() throws Exception {

    }

    @Test
    @DisplayName("Career 등록")
    @WithAccount("yk")
    public void newCareer() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorRepository.save(tutor);

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

        List<Career> careers = tutor.getCareers();
        assertEquals(1, careers.size());

        Career career = careers.get(0);
        assertEquals("tutorlab", career.getCompanyName());
        assertEquals("engineer", career.getDuty());
        assertEquals(LocalDate.parse("2007-12-03"), career.getStartDate());
        assertEquals(LocalDate.parse("2007-12-04"), career.getEndDate());
        assertEquals(false, career.isPresent());

    }

    // TODO
    @Test
    @DisplayName("Career 등록 - Invalid Input")
    public void newCareer_withInvalidInput() throws Exception {

        // Given

        // When

        // Then
    }

    // TODO
    @Test
    @DisplayName("Career 등록 - 인증된 사용자 X")
    public void newCareer_withoutAuthenticatedUser() throws Exception {

        // Given

        // When

        // Then
    }

    @Test
    @DisplayName("Career 수정")
    @WithAccount("yk")
    public void editCareer() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorRepository.save(tutor);

        Career career = Career.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate(LocalDate.parse("2007-12-03"))
                .endDate(LocalDate.parse("2007-12-04"))
                .present(false)
                .build();
        careerRepository.save(career);
        tutor.addCareer(career);

        // When
        Long careerId = career.getId();
        CareerUpdateRequest careerUpdateRequest = CareerUpdateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-05")
                .present(true)
                .build();

        mockMvc.perform(put("/careers/" + careerId)
        .content(objectMapper.writeValueAsString(careerUpdateRequest))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

        // Then
        List<Career> careers = tutor.getCareers();
        assertEquals(1, careers.size());

        assertEquals("tutorlab", career.getCompanyName());
        assertEquals("engineer", career.getDuty());
        assertEquals(LocalDate.parse("2007-12-03"), career.getStartDate());
        assertEquals(LocalDate.parse("2007-12-05"), career.getEndDate());
        assertEquals(true, career.isPresent());
    }

    @Test
    @DisplayName("Career 삭제")
    @WithAccount("yk")
    public void removeCareer() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorRepository.save(tutor);

        Career career = Career.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate(LocalDate.parse("2007-12-03"))
                .endDate(LocalDate.parse("2007-12-04"))
                .present(false)
                .build();
        careerRepository.save(career);
        tutor.addCareer(career);

        // When
        Long careerId = career.getId();
        mockMvc.perform(delete("/careers/" + careerId))
        .andDo(print())
        .andExpect(status().isOk());

        // Then
        // TODO - CHECK : career가 존재하는 이유
        System.out.println(career);
        assertEquals(0, tutor.getCareers().size());
        assertEquals(null, career.getTutor());
    }
}