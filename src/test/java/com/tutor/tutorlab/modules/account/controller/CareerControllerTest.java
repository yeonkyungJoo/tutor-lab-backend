package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.response.CareerResponse;
import com.tutor.tutorlab.modules.account.service.CareerService;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.User;
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

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.CAREER;
import static com.tutor.tutorlab.configuration.AbstractTest.*;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTOR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private ObjectMapper objectMapper = new ObjectMapper();

    private Career career = Mockito.mock(Career.class);
    private CareerResponse careerResponse;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(careerController)
                .setControllerAdvice(RestControllerExceptionAdvice.class).build();
    }

    // TODO - 인증되지 않은 사용자

    @Test
    void getCareer() throws Exception {

        // given
        careerResponse = new CareerResponse(career);
        careerResponse.setJob("engineer");
        careerResponse.setCompanyName("google");
        careerResponse.setOthers(null);
        careerResponse.setLicense(null);

        when(careerService.getCareerResponse(any(User.class), anyLong()))
                .thenReturn(careerResponse);
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
    void _newCareer() throws Exception {

        // given
        when(careerService.createCareer(any(User.class), any(CareerCreateRequest.class)))
                .thenReturn(career);
        // when
        // then
        CareerCreateRequest createRequest = getCareerCreateRequest();
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("UnauthorizedException 발생")
    @Test
    void newCareer_withUnauthorizedException() throws Exception {

        // given
        when(careerService.createCareer(any(User.class), any(CareerCreateRequest.class)))
                .thenThrow(new UnauthorizedException(TUTOR));
        // when
        // then
        CareerCreateRequest createRequest = getCareerCreateRequest();
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void newCareer() {

        // given
        CareerCreateRequest createRequest = getCareerCreateRequest();

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