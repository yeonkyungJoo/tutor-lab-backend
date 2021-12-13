package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.config.security.PrincipalDetailsService;
import com.tutor.tutorlab.config.security.jwt.JwtRequestFilter;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.modules.account.controller.response.CareerResponse;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.CareerService;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.CAREER;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTOR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CareerControllerTest {

    private final static String BASE_URL = "/careers";

    @Mock
    CareerService careerService;
    @InjectMocks
    CareerController careerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(careerController)
                .setControllerAdvice(RestControllerExceptionAdvice.class).build();
    }

    // TODO - 인증되지 않은 사용자

    @Test
    void getCareer() throws Exception {

        // given
        when(careerService.getCareerResponse(any(User.class), anyLong()))
                .thenAnswer(new Answer<CareerResponse>() {
                    @Override
                    public CareerResponse answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });

        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{career_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 예외 발생 시
    @Test
    void getCareer_withUnauthorizedException() throws Exception {

        // given
        when(careerService.getCareerResponse(any(User.class), anyLong()))
                .thenThrow(new UnauthorizedException(TUTOR));
                //.thenThrow(UnauthorizedException.class);

        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{career_id}", 1L))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getCareer_withEntityNotFoundException() throws Exception {

        // given
        when(careerService.getCareerResponse(any(User.class), anyLong()))
                .thenThrow(new EntityNotFoundException(CAREER));
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{career_id}", 1L))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void newCareer() {

        // given
        // when
        // then
    }

    @Test
    void editCareer() {

        // given
        // when
        // then
    }

    @Test
    void deleteCareer() {

        // given
        // when
        // then
    }
}