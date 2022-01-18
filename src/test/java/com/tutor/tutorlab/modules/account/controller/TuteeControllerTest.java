package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.config.interceptor.AuthInterceptor;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.jwt.JwtRequestFilter;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.service.TuteeService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ContextConfiguration
//@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class TuteeControllerTest {

    private final static String BASE_URL = "/tutees";

    @Mock
    TuteeService tuteeService;
    @InjectMocks
    TuteeController tuteeController;

//    @Autowired
//    WebApplicationContext context;

    @InjectMocks
    AuthInterceptor authInterceptor;
//    @InjectMocks
//    JwtRequestFilter jwtRequestFilter;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
                .standaloneSetup(tuteeController)
//                .addFilter(jwtRequestFilter)
                .addInterceptors(authInterceptor)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();
        assertNotNull(mockMvc);
    }

    @Test
    void getTutees() throws Exception {

        // given
        Tutee tutee = Tutee.of(mock(User.class));
        Page<TuteeResponse> response = new PageImpl<>(Arrays.asList(new TuteeResponse(tutee)), Pageable.ofSize(20), 1);
        doReturn(response)
                .when(tuteeService).getTuteeResponses(anyInt());

        // when
        // then
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-mockmvc.html
//    @Test
//    void editTutee_withoutAuth() throws Exception {
//
//        // given
////        doNothing()
////                .when(tuteeService).updateTutee(any(User.class), any(TuteeUpdateRequest.class));
//        // when
//        // then
//        TuteeUpdateRequest request = AbstractTest.getTuteeUpdateRequest();
//        mockMvc.perform(put(BASE_URL + "/my-info").with(anonymous())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }

    @Test
    void editTutee() throws Exception {

        // given
        doNothing()
                .when(tuteeService).updateTutee(any(User.class), any(TuteeUpdateRequest.class));
        // when
        // then
        User user = User.of(
                "user@email.com",
                "password",
                "user", null, null, null, "user@email.com",
                "user", null, null, null, RoleType.TUTEE,
                null, null
        );
        PrincipalDetails principal = new PrincipalDetails(user);
        TuteeUpdateRequest request = AbstractTest.getTuteeUpdateRequest();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities()));
        mockMvc.perform(put(BASE_URL + "/my-info").with(securityContext(securityContext))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}