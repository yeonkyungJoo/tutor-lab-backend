package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.CareerResponse;
import com.tutor.tutorlab.modules.account.controller.response.EducationResponse;
import com.tutor.tutorlab.modules.account.controller.response.TutorResponse;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TutorControllerTest {

    private final static String BASE_URL = "/tutors";

    @InjectMocks
    TutorController tutorController;
    @Mock
    TutorService tutorService;
    @Mock
    TutorLectureService tutorLectureService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tutorController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @Test
    void getTutors() throws Exception {

        // given
        Page<TutorResponse> tutors =
                new PageImpl<>(Arrays.asList(Mockito.mock(TutorResponse.class), Mockito.mock(TutorResponse.class)), Pageable.ofSize(20), 2);
        doReturn(tutors)
                .when(tutorService).getTutorResponses(1);
        // when
        // then
        mockMvc.perform(get(BASE_URL, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutors)));
    }

    // TODO - @CurrentUser 테스트
    @Test
    void getMyInfo() throws Exception {

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        TutorResponse tutorResponse = new TutorResponse(tutor);
        doReturn(tutorResponse)
                .when(tutorService).getTutorResponse(any(User.class));
        // when
        // then
        mockMvc.perform(get(BASE_URL, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutorResponse)));
    }

    @Test
    void getTutor() throws Exception {

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        TutorResponse tutorResponse = new TutorResponse(tutor);
        doReturn(tutorResponse)
                .when(tutorService).getTutorResponse(1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{tutor_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutorResponse)));
    }

    @Test
    void newTutor() throws Exception {

        // given
        doNothing()
                .when(tutorService).createTutor(any(User.class), any(TutorSignUpRequest.class));
        // when
        // then
        TutorSignUpRequest tutorSignUpRequest = AbstractTest.getTutorSignUpRequest();
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tutorSignUpRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void editTutor() throws Exception {

        // given
        doNothing()
                .when(tutorService).updateTutor(any(User.class), any(TutorUpdateRequest.class));
        // when
        // then
        TutorUpdateRequest tutorUpdateRequest = AbstractTest.getTutorUpdateRequest();
        mockMvc.perform(put(BASE_URL + "/my-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tutorUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void quitTutor() throws Exception {

        // given
        doNothing()
                .when(tutorService).deleteTutor(any(User.class));
        // when
        // then
        mockMvc.perform(delete(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getCareers() throws Exception {

        // given
        List<CareerResponse> careers = Arrays.asList(Mockito.mock(CareerResponse.class), Mockito.mock(CareerResponse.class));
        doReturn(careers)
                .when(tutorService).getCareerResponses(1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{tutor_id}/careers", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.job").exists())
                .andExpect(jsonPath("$.companyName").exists())
                .andExpect(jsonPath("$.others").exists())
                .andExpect(jsonPath("$.license").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(careers)));
    }

    // test - json path
    @Test
    void getEducations() throws Exception {

        // given
        List<EducationResponse> educations
                = Arrays.asList(Mockito.mock(EducationResponse.class), Mockito.mock(EducationResponse.class));
        doReturn(educations)
                .when(tutorService).getEducationResponses(1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{tutor_id}/educations", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.educationLevel").exists())
                .andExpect(jsonPath("$.schoolName").exists())
                .andExpect(jsonPath("$.major").exists())
                .andExpect(jsonPath("$.others").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(educations)));
    }

    @Test
    void getLectures() throws Exception {

        // given
        Page<LectureResponse> lectures =
                new PageImpl<>(Arrays.asList(Mockito.mock(LectureResponse.class), Mockito.mock(LectureResponse.class)), Pageable.ofSize(20), 2);
        doReturn(lectures)
                .when(tutorLectureService).getLectureResponses(1L, 1);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{tutor_id}/lectures", 1L, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lectures)));
    }
}