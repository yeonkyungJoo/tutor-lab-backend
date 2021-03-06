package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentResponse;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TutorLectureControllerTest {

    private final static String BASE_URL = "/api/tutors/my-lectures";

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
        Lecture lecture = mock(Lecture.class);
        Tutor tutor = mock(Tutor.class);
        when(tutor.getUser()).thenReturn(mock(User.class));
        when(lecture.getTutor()).thenReturn(tutor);
        LectureResponse lectureResponse = new LectureResponse(lecture);
        Page<LectureResponse> lectures = new PageImpl<>(Arrays.asList(lectureResponse), Pageable.ofSize(20), 1);
        doReturn(lectures)
                .when(tutorLectureService).getLectureResponses(any(User.class), anyInt());
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
        Lecture lecture = mock(Lecture.class);
        Tutor tutor = mock(Tutor.class);
        when(tutor.getUser()).thenReturn(mock(User.class));
        when(lecture.getTutor()).thenReturn(tutor);
        LectureResponse response = new LectureResponse(lecture);
        doReturn(response)
                .when(lectureService).getLectureResponse(any(User.class), anyLong());
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.thumbnail").hasJsonPath())
                .andExpect(jsonPath("$.title").hasJsonPath())
                .andExpect(jsonPath("$.subTitle").hasJsonPath())
                .andExpect(jsonPath("$.introduce").hasJsonPath())
                .andExpect(jsonPath("$.content").hasJsonPath())
                .andExpect(jsonPath("$.difficultyType").hasJsonPath())
                .andExpect(jsonPath("$.systemTypes").hasJsonPath())
                .andExpect(jsonPath("$.lecturePrices").hasJsonPath())
                .andExpect(jsonPath("$.lectureSubjects").hasJsonPath())
                .andExpect(jsonPath("$.reviewCount").hasJsonPath())
                .andExpect(jsonPath("$.scoreAverage").hasJsonPath())
                .andExpect(jsonPath("$.lectureTutor").hasJsonPath())
                // ????????? ?????? ??????
                .andExpect(jsonPath("$.picked").hasJsonPath())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getReviewsOfLecture() throws Exception {

        // given
        Review parent1 = mock(Review.class);
        when(parent1.getUser()).thenReturn(mock(User.class));
        ReviewResponse reviewResponse1 = new ReviewResponse(parent1, null);

        Review parent2 = mock(Review.class);
        when(parent2.getUser()).thenReturn(mock(User.class));
        Review child = mock(Review.class);
        when(child.getUser()).thenReturn(mock(User.class));
        ReviewResponse reviewResponse2 = new ReviewResponse(parent2, child);

        Page<ReviewResponse> reviews =
                new PageImpl<>(Arrays.asList(reviewResponse1, reviewResponse2), Pageable.ofSize(20), 2);
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
        Review parent = mock(Review.class);
        when(parent.getUser()).thenReturn(mock(User.class));
        ReviewResponse response = new ReviewResponse(parent, null);
        doReturn(response)
                .when(reviewService).getReviewResponseOfLecture(1L, 1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/reviews/{review_id}", 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").hasJsonPath())
                .andExpect(jsonPath("$.score").hasJsonPath())
                .andExpect(jsonPath("$.content").hasJsonPath())
                .andExpect(jsonPath("$.username").hasJsonPath())
                .andExpect(jsonPath("$.userNickname").hasJsonPath())
                .andExpect(jsonPath("$.createdAt").hasJsonPath())
                .andExpect(jsonPath("$.child").hasJsonPath())
                .andExpect(jsonPath("$.child.reviewId").hasJsonPath())
                .andExpect(jsonPath("$.child.content").hasJsonPath())
                .andExpect(jsonPath("$.child.username").hasJsonPath())
                .andExpect(jsonPath("$.child.userNickname").hasJsonPath())
                .andExpect(jsonPath("$.child.createdAt").hasJsonPath())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getReviewOfLecture() throws Exception {

        // given
        Review parent = mock(Review.class);
        when(parent.getUser()).thenReturn(mock(User.class));
        Review child = mock(Review.class);
        when(child.getUser()).thenReturn(mock(User.class));
        ReviewResponse response = new ReviewResponse(parent, child);
        doReturn(response)
                .when(reviewService).getReviewResponseOfLecture(1L, 1L);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/reviews/{review_id}", 1L, 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").hasJsonPath())
                .andExpect(jsonPath("$.score").hasJsonPath())
                .andExpect(jsonPath("$.content").hasJsonPath())
                .andExpect(jsonPath("$.username").hasJsonPath())
                .andExpect(jsonPath("$.userNickname").hasJsonPath())
                .andExpect(jsonPath("$.createdAt").hasJsonPath())
                .andExpect(jsonPath("$.child").hasJsonPath())
                .andExpect(jsonPath("$.child.reviewId").hasJsonPath())
                .andExpect(jsonPath("$.child.content").hasJsonPath())
                .andExpect(jsonPath("$.child.username").hasJsonPath())
                .andExpect(jsonPath("$.child.userNickname").hasJsonPath())
                .andExpect(jsonPath("$.child.createdAt").hasJsonPath())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void newReview() throws Exception {

        // given
//        User user = User.of(
//                "user@email.com",
//                "password",
//                "user", null, null, null, "user@email.com",
//                "user", null, null, null, RoleType.TUTEE,
//                null, null
//        );
//        PrincipalDetails principal = new PrincipalDetails(user);
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities()));

        Review review = mock(Review.class);
        doReturn(review)
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
        User user = User.of(
                "user@email.com",
                "password",
                "user", null, null, null, "user@email.com",
                "user", null, null, null, RoleType.TUTEE,
                null, null
        );
        PrincipalDetails principal = new PrincipalDetails(user);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities()));

        Tutee tutee = mock(Tutee.class);
        when(tutee.getUser()).thenReturn(mock(User.class));
        TuteeResponse tuteeResponse = new TuteeResponse(tutee);
        Page<TuteeResponse> tutees =
                new PageImpl<>(Arrays.asList(tuteeResponse), Pageable.ofSize(20), 1);
        doReturn(tutees)
                .when(tutorLectureService).getTuteeResponsesOfLecture(user, 1L, 1);
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
        User user = User.of(
                "user@email.com",
                "password",
                "user", null, null, null, "user@email.com",
                "user", null, null, null, RoleType.TUTEE,
                null, null
        );
        PrincipalDetails principal = new PrincipalDetails(user);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities()));

        Enrollment enrollment = mock(Enrollment.class);
        Tutee tutee = mock(Tutee.class);
        when(tutee.getUser()).thenReturn(mock(User.class));
        when(enrollment.getTutee()).thenReturn(tutee);
        when(enrollment.getLecture()).thenReturn(mock(Lecture.class));
        EnrollmentResponse enrollmentResponse = new EnrollmentResponse(enrollment);
        Page<EnrollmentResponse> enrollments = new PageImpl<>(Arrays.asList(enrollmentResponse), Pageable.ofSize(20), 1);
        doReturn(enrollments)
                .when(tutorLectureService).getEnrollmentResponsesOfLecture(user, 1L, 1);
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/enrollments", 1L, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(enrollments)));
    }
}