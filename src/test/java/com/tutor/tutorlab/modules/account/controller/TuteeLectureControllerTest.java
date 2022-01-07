package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.purchase.controller.request.CancellationCreateRequest;
import com.tutor.tutorlab.modules.purchase.service.CancellationService;
import com.tutor.tutorlab.modules.purchase.service.CancellationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TuteeLectureControllerTest {

    private final static String BASE_URL = "/tutees/my-lectures";

    @InjectMocks
    TuteeLectureController tuteeLectureController;
    @Mock
    LectureService lectureService;
    @Mock
    CancellationServiceImpl cancellationService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tuteeLectureController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @DisplayName("(= 강의 개별 조회)")
    @Test
    void getLecture() throws Exception {

        // given
        when(lectureService.getLectureResponse(anyLong()))
                .thenReturn(Mockito.mock(LectureResponse.class));
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void cancel() throws Exception {

        // given
        doNothing()
                .when(cancellationService).cancel(any(User.class), anyLong(), any(CancellationCreateRequest.class));

        // when
        // then
        CancellationCreateRequest cancellationCreateRequest = CancellationCreateRequest.of("감사합니다!");
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/cancellations", 1L)
                .content(objectMapper.writeValueAsString(cancellationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getReviewsOfLecture() throws Exception {

        // given
        Page<Object> pages = Page.empty();
        // doReturn()
        // when
        // then
    }

    @Test
    void getReviewOfLecture() throws Exception {

        // given

        // when
        // then
    }

    @Test
    void newReview() throws Exception {

        // given

        // when
        // then
    }

    @Test
    void editReview() throws Exception {

        // given

        // when
        // then
    }

    @Test
    void deleteReview() throws Exception {

        // given

        // when
        // then
    }

    @Test
    void close() throws Exception {

        // given

        // when
        // then
    }
}