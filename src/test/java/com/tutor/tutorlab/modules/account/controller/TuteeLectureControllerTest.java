package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TuteeLectureControllerTest {

    private final static String BASE_URL = "/tutees/my-lectures";

    @InjectMocks
    TuteeLectureController tuteeLectureController;
    @Mock
    LectureService lectureService;

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
    void getLecture() {

        // given
        when(lectureService.getLectureResponse(anyLong()))
                .thenReturn(Mockito.mock(LectureResponse.class));
        // when
        // mockMvc.perform()
        // then
    }

    @Test
    void cancel() {
    }

    @Test
    void getReviewsOfLecture() {
    }

    @Test
    void getReviewOfLecture() {
    }

    @Test
    void newReview() {
    }

    @Test
    void editReview() {
    }

    @Test
    void deleteReview() {
    }

    @Test
    void close() {
    }
}