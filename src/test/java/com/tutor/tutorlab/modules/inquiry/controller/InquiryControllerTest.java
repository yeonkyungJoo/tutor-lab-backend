package com.tutor.tutorlab.modules.inquiry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.inquiry.controller.request.InquiryCreateRequest;
import com.tutor.tutorlab.modules.inquiry.enums.InquiryType;
import com.tutor.tutorlab.modules.inquiry.service.InquiryService;
import com.tutor.tutorlab.modules.inquiry.vo.Inquiry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InquiryControllerTest {

    private final static String BASE_URL = "/users/my-inquiry";

    @InjectMocks
    InquiryController inquiryController;
    @Mock
    InquiryService inquiryService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(inquiryController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @Test
    void newInquiry() throws Exception {

        // given
        doReturn(Mockito.mock(Inquiry.class))
                .when(inquiryService).createInquiry(any(User.class), any(InquiryCreateRequest.class));
        // when
        // then
        InquiryCreateRequest inquiryCreateRequest = InquiryCreateRequest.of(InquiryType.TUTEE, "제목", "내용");
        mockMvc.perform(post(BASE_URL)
                .content(objectMapper.writeValueAsString(inquiryCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }



//    @DisplayName("RabbitMQ 테스트")
//    @Test
//    void test() throws Exception {
//
//        // given
//        doReturn(Mockito.mock(Inquiry.class))
//                .when(inquiryService).test(any(InquiryCreateRequest.class));
//        // when
//        // then
//        InquiryCreateRequest inquiryCreateRequest = InquiryCreateRequest.of(InquiryType.TUTEE, "제목", "내용");
//        mockMvc.perform(post(BASE_URL + "/test-producer")
//                .content(objectMapper.writeValueAsString(inquiryCreateRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}