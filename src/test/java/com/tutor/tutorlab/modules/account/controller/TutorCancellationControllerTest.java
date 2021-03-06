package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.modules.account.service.TutorCancellationService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.controller.response.CancellationResponse;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TutorCancellationControllerTest {

    private final static String BASE_URL = "/api/tutors/my-cancellations";

    @InjectMocks
    TutorCancellationController tutorCancellationController;
    @Mock
    TutorCancellationService tutorCancellationService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tutorCancellationController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @Test
    void getMyCancellations() throws Exception {

        // given
        CancellationResponse cancellationResponse = CancellationResponse.builder()
                .cancellation(mock(Cancellation.class))
                .lecture(mock(Lecture.class))
                .lecturePrice(mock(LecturePrice.class))
                .tuteeId(1L)
                .tuteeName("user")
                .chatroomId(1L)
                .build();
        Page<CancellationResponse> cancellations = new PageImpl<>(Arrays.asList(cancellationResponse), Pageable.ofSize(20), 1);
        doReturn(cancellations)
                .when(tutorCancellationService).getCancellationResponses(any(User.class), anyInt());
        // when
        // then
        mockMvc.perform(get(BASE_URL, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cancellations)));
    }

    @Test
    void approveCancellation() throws Exception {

        // given
        doNothing()
                .when(tutorCancellationService).approve(any(User.class), anyLong());
        // when
        // then
        mockMvc.perform(put(BASE_URL + "/{cancellation_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }
}