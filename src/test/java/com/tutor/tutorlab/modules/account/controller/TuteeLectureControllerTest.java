package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.controller.request.CancellationCreateRequest;
import com.tutor.tutorlab.modules.purchase.service.CancellationService;
import com.tutor.tutorlab.modules.purchase.service.CancellationServiceImpl;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentServiceImpl;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import com.tutor.tutorlab.modules.review.vo.Review;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TuteeLectureControllerTest {

    private final static String BASE_URL = "/api/tutees/my-lectures";

    @InjectMocks
    TuteeLectureController tuteeLectureController;
    @Mock
    LectureService lectureService;
    @Mock
    CancellationServiceImpl cancellationService;
    @Mock
    ReviewService reviewService;
    @Mock
    EnrollmentServiceImpl enrollmentService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tuteeLectureController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @DisplayName("(= ?????? ?????? ??????)")
    @Test
    void getLecture() throws Exception {

        // given
        Tutor tutor = Tutor.of(mock(User.class));
        Lecture lecture = Lecture.of(
                tutor,
                "title",
                "subTitle",
                "introduce",
                "content",
                DifficultyType.ADVANCED,
                Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE),
                "thumbnail"
        );
        LectureResponse lectureResponse = new LectureResponse(lecture);
        when(lectureService.getLectureResponse(any(User.class), anyLong())).thenReturn(lectureResponse);

        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lectureResponse)));
    }

    @Test
    void cancel() throws Exception {

        // given
        doReturn(mock(Cancellation.class))
                .when(cancellationService).cancel(any(User.class), anyLong(), any(CancellationCreateRequest.class));

        // when
        // then
        CancellationCreateRequest cancellationCreateRequest = CancellationCreateRequest.of("???????????????!");
        mockMvc.perform(post(BASE_URL + "/{lecture_id}/cancellations", 1L)
                .content(objectMapper.writeValueAsString(cancellationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getReviewOfLecture() throws Exception {

        // given
        Review parent = Review.of(5, "content", mock(User.class), mock(Enrollment.class), mock(Lecture.class), null);
        Review child = Review.of(
                null, "content_", mock(User.class), null, mock(Lecture.class), parent
        );
        ReviewResponse response = new ReviewResponse(parent, child);
        doReturn(response)
                .when(reviewService).getReviewResponseOfLecture(anyLong(), anyLong());
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/reviews/{review_id}", 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void newReview() throws Exception {

        // given
        doReturn(mock(Review.class))
                .when(reviewService).createTuteeReview(any(User.class), anyLong(), any(TuteeReviewCreateRequest.class));

        // when
        // then
        TuteeReviewCreateRequest tuteeReviewCreateRequest = AbstractTest.getTuteeReviewCreateRequest();
        mockMvc.perform(post(BASE_URL + "/{lecture_id}/reviews", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tuteeReviewCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void editReview() throws Exception {

        // given
        doNothing()
                .when(reviewService).updateTuteeReview(any(User.class), anyLong(), anyLong(), any(TuteeReviewUpdateRequest.class));

        // when
        // then
        TuteeReviewUpdateRequest tuteeReviewUpdateRequest = AbstractTest.getTuteeReviewUpdateRequest();
        mockMvc.perform(put(BASE_URL + "/{lecture_id}/reviews/{review_id}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tuteeReviewUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteReview() throws Exception {

        // given
        doNothing()
                .when(reviewService).deleteTuteeReview(any(User.class), anyLong(), anyLong());
        // when
        // then
        mockMvc.perform(delete(BASE_URL + "/{lecture_id}/reviews/{review_id}", 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void close() throws Exception {

        // given
        doNothing()
                .when(enrollmentService).close(any(User.class), anyLong());
        // when
        // then
        mockMvc.perform(put(BASE_URL + "/{lecture_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }
}