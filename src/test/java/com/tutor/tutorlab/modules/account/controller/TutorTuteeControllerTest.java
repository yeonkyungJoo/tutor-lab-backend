package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.modules.account.controller.response.TuteeLectureResponse;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.service.TutorTuteeService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrolledLectureResponse;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TutorTuteeControllerTest {

    private final static String BASE_URL = "/api/tutors/my-tutees";

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

        // tuteeId, userId, name
        TuteeSimpleResponse tuteeSimpleResponse = TuteeSimpleResponse.builder()
                .tuteeId(1L)
                .userId(1L)
                .name("user")
                .build();
        Page<TuteeSimpleResponse> tutees = new PageImpl<>(Arrays.asList(tuteeSimpleResponse), Pageable.ofSize(20), 1);
        doReturn(tutees)
                .when(tutorTuteeService).getTuteeSimpleResponses(user, false, 1);
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
        TuteeLectureResponse tuteeLectureResponse = TuteeLectureResponse.builder()
                .tuteeId(1L)
                .lecture(Mockito.mock(Lecture.class))
                .lecturePrice(Mockito.mock(LecturePrice.class))
                .reviewId(1L)
                .chatroomId(1L)
                .build();
        Page<TuteeLectureResponse> tuteeLectures =
                new PageImpl<>(Arrays.asList(tuteeLectureResponse), Pageable.ofSize(20), 2);
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
        when(review.getUser()).thenReturn(Mockito.mock(User.class));
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