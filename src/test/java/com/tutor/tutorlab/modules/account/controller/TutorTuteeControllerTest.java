package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.modules.account.controller.response.TuteeLectureResponse;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.service.TutorTuteeService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import com.tutor.tutorlab.modules.review.vo.Review;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TutorTuteeControllerTest {

    private final static String BASE_URL = "/tutors/my-tutees";

    @InjectMocks
    TutorTuteeController tutorTuteeController;
    @Mock
    TutorTuteeService tutorTuteeService;
    @Mock
    ReviewService reviewService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tutorTuteeController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @Test
    void getMyTutees() throws Exception {

        // given
        Page<TuteeSimpleResponse> tutees =
                new PageImpl<>(Arrays.asList(Mockito.mock(TuteeSimpleResponse.class), Mockito.mock(TuteeSimpleResponse.class)), Pageable.ofSize(20), 2);
        doReturn(tutees)
                .when(tutorTuteeService).getTuteeSimpleResponses(any(User.class), false, 1);
        // when
        // then
        mockMvc.perform(get(BASE_URL, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutees)));
    }

    // call real-method
    @Test
    void getMyTutee() throws Exception {

        // given
        Page<TuteeLectureResponse> tuteeLectures =
                new PageImpl<>(Arrays.asList(Mockito.mock(TuteeLectureResponse.class), Mockito.mock(TuteeLectureResponse.class)), Pageable.ofSize(20), 2);
        doReturn(tuteeLectures)
                .when(tutorTuteeService).getTuteeLectureResponses(any(User.class), anyBoolean(), anyLong(), anyInt());
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{tutee_id}", 1)
                .param("closed", "false")
                .param("page", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tuteeLectures)));
    }

    @Test
    void getReviewsOfMyTutee() throws Exception {

        // given
        Review review = Mockito.mock(Review.class);
        ReviewResponse response = new ReviewResponse(review, null);
        doReturn(response)
                .when(reviewService).getReviewResponseOfLecture(1L, 1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{tutee_id}/lectures/{lecture_id}/reviews/{review_id}", 1L, 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}