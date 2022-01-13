package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentResponse;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TutorLectureControllerTest {

    private final static String BASE_URL = "/tutors/my-lectures";

    @InjectMocks
    TutorLectureController tutorLectureController;
    @Mock
    TutorLectureService tutorLectureService;
    @Mock
    LectureService lectureService;
    @Mock
    ReviewService reviewService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tutorLectureController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @Test
    void getLectures() throws Exception {

        // given
        Page<LectureResponse> lectures =
                new PageImpl<>(Arrays.asList(Mockito.mock(LectureResponse.class), Mockito.mock(LectureResponse.class)), Pageable.ofSize(20), 2);
        doReturn(lectures)
                .when(tutorLectureService).getLectureResponses(any(User.class), 1);
        // when
        // then
        mockMvc.perform(get(BASE_URL, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lectures)));
    }

    @Test
    void getLecture() throws Exception {

        // given
        Lecture lecture = Mockito.mock(Lecture.class);
        LectureResponse response = new LectureResponse(lecture);
        doReturn(response)
                .when(lectureService).getLectureResponse(1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.thumbnail").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.subTitle").exists())
                .andExpect(jsonPath("$.introduce").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.difficultyType").exists())
                .andExpect(jsonPath("$.systemTypes").exists())
                .andExpect(jsonPath("$.lecturePrices").exists())
                .andExpect(jsonPath("$.lectureSubjects").exists())
                .andExpect(jsonPath("$.reviewCount").exists())
                .andExpect(jsonPath("$.scoreAverage").exists())
                .andExpect(jsonPath("$.lectureTutor").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getReviewsOfLecture() throws Exception {

        // given
        Page<ReviewResponse> reviews =
                new PageImpl<>(Arrays.asList(Mockito.mock(ReviewResponse.class), Mockito.mock(ReviewResponse.class)), Pageable.ofSize(20), 2);
        doReturn(reviews)
                .when(reviewService).getReviewResponsesOfLecture(1L, 1);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/reviews", 1L, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reviews)));
    }

    @Test
    void getReviewOfLecture_when_child_isNull() throws Exception {

        // given
        Review parent = Mockito.mock(Review.class);
        ReviewResponse response = new ReviewResponse(parent, null);
        doReturn(response)
                .when(reviewService).getReviewResponseOfLecture(1L, 1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/reviews/{review_id}", 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.score").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.userNickname").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.child").exists())
                .andExpect(jsonPath("$.child.reviewId").exists())
                .andExpect(jsonPath("$.child.content").exists())
                .andExpect(jsonPath("$.child.username").exists())
                .andExpect(jsonPath("$.child.userNickname").exists())
                .andExpect(jsonPath("$.child.createdAt").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getReviewOfLecture() throws Exception {

        // given
        Review parent = Mockito.mock(Review.class);
        Review child = Mockito.mock(Review.class);
        ReviewResponse response = new ReviewResponse(parent, child);
        doReturn(response)
                .when(reviewService).getReviewResponseOfLecture(1L, 1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/reviews/{review_id}", 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.score").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.userNickname").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.child").exists())
                .andExpect(jsonPath("$.child.reviewId").exists())
                .andExpect(jsonPath("$.child.content").exists())
                .andExpect(jsonPath("$.child.username").exists())
                .andExpect(jsonPath("$.child.userNickname").exists())
                .andExpect(jsonPath("$.child.createdAt").exists())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void newReview() throws Exception {

        // given
        doNothing()
                .when(reviewService).createTutorReview(any(User.class), anyLong(), anyLong(), any(TutorReviewCreateRequest.class));
        // when
        // then
        TutorReviewCreateRequest tutorReviewCreateRequest = AbstractTest.getTutorReviewCreateRequest();
        mockMvc.perform(post(BASE_URL + "/{lecture_id}/reviews/{parent_id}", 1L, 1L)
                .content(objectMapper.writeValueAsString(tutorReviewCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void editReview() throws Exception {

        // given
        doNothing()
                .when(reviewService).updateTutorReview(any(User.class), anyLong(), anyLong(), anyLong(), any(TutorReviewUpdateRequest.class));
        // when
        // then
        TutorReviewUpdateRequest tutorReviewUpdateRequest = AbstractTest.getTutorReviewUpdateRequest();
        mockMvc.perform(put(BASE_URL + "/{lecture_id}/reviews/{parent_id}/children/{review_id}", 1L, 1L, 2L)
                .content(objectMapper.writeValueAsString(tutorReviewUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteReview() throws Exception {

        // given
        doNothing()
                .when(reviewService).deleteTutorReview(any(User.class), anyLong(), anyLong(), anyLong());
        // when
        // then
        mockMvc.perform(delete(BASE_URL + "/{lecture_id}/reviews/{parent_id}/children/{review_id}", 1L, 1L, 2L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getTuteesOfLecture() throws Exception {

        // given
        Page<TuteeResponse> tutees =
                new PageImpl<>(Arrays.asList(Mockito.mock(TuteeResponse.class)), Pageable.ofSize(20), 1);
        doReturn(tutees)
                .when(tutorLectureService).getTuteeResponsesOfLecture(any(User.class), 1L, 1);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/tutees", 1L, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutees)));
    }

    @Test
    void getEnrollmentsOfLecture() throws Exception {

        // given
        Page<EnrollmentResponse> enrollments =
                new PageImpl<>(Arrays.asList(Mockito.mock(EnrollmentResponse.class)), Pageable.ofSize(20), 1);
        doReturn(enrollments)
                .when(tutorLectureService).getEnrollmentResponsesOfLecture(any(User.class), 1L, 1);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/enrollments", 1L, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(enrollments)));
    }
}