package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.config.interceptor.AuthInterceptor;
import com.tutor.tutorlab.config.security.jwt.JwtRequestFilter;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.service.TuteeService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TuteeControllerTest {

    private final static String BASE_URL = "/tutees";

    @Mock
    TuteeService tuteeService;
    @InjectMocks
    TuteeController tuteeController;
    @InjectMocks
    AuthInterceptor authInterceptor;
    @InjectMocks
    JwtRequestFilter jwtRequestFilter;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tuteeController)
                .addFilter(jwtRequestFilter)
                .addInterceptors(authInterceptor)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
    }

    @Test
    void getTutees() throws Exception {

        // given
        assertNotNull(mockMvc);
        // System.out.println(authInterceptor);
        Page response = Page.empty();
        doReturn(response)
                .when(tuteeService).getTuteeResponses(anyInt());

        // when
        // then
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void editTutee_withoutAuth() throws Exception {

        // given
//        doNothing()
//                .when(tuteeService).updateTutee(any(User.class), any(TuteeUpdateRequest.class));
        // when
        // then
        TuteeUpdateRequest request = AbstractTest.getTuteeUpdateRequest();
        mockMvc.perform(put(BASE_URL + "/my-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // TODO
    @Test
    @WithMockUser
    void editTutee() throws Exception {

        // given
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(jwtRequestFilter);
//        doNothing()
//                .when(tuteeService).updateTutee(any(User.class), any(TuteeUpdateRequest.class));
        // when
        // then
//        TuteeUpdateRequest request = AbstractTest.getTuteeUpdateRequest();
//        mockMvc.perform(put(BASE_URL + "/my-info")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk());
    }
}