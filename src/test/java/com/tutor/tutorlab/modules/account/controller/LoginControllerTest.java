package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpOAuthDetailRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.service.CareerService;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    LoginService loginService;
    @InjectMocks
    LoginController loginController;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(RestControllerExceptionAdvice.class).build();
    }

    @Test
    void oauth() throws Exception {

        // given
        // when
        // then
        String provider = "kakao";
        MvcResult result = mockMvc.perform(get("/oauth/{provider}", provider))
                .andDo(print())
                .andExpect(redirectedUrl("https://kauth.kakao.com/oauth/authorize?client_id=8dc9eea7e202a581e0449058e753beaf&redirect_uri=http://localhost:8080/oauth/kakao/callback&response_type=code"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        System.out.println(result);
    }

    @Test
    void oauth_unsupported() throws Exception {

        // given
        // when
        // then
        String provider = "facebook";
        mockMvc.perform(get("/oauth/{provider}", provider))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    void oauthCallback() throws Exception {

        // given
        // when
        // then
    }

    @Test
    void oauthCallback_returnNull() throws Exception {

        // given
        // when
        // then
    }

    @Test
    void _oauth() throws Exception {

        // given
        // when
        // then
    }

    @DisplayName("OAuth 회원가입 추가 정보 입력")
    @Test
    void signUpOAuthDetail() throws Exception {

        // given
        doNothing()
                .when(loginService).signUpOAuthDetail(any(User.class), any(SignUpOAuthDetailRequest.class));
        // when
        // then
        SignUpOAuthDetailRequest request = AbstractTest.getSignUpOAuthDetailRequest("user");
        mockMvc.perform(post("/sign-up/oauth/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void signUpOAuthDetail_invalid() throws Exception {

        // given
//        doNothing()
//                .when(loginService).signUpOAuthDetail(any(User.class), any(SignUpOAuthDetailRequest.class));
        // when
        // then
        SignUpOAuthDetailRequest request = AbstractTest.getSignUpOAuthDetailRequest("user");
        request.setEmail("user");
        mockMvc.perform(post("/sign-up/oauth/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입")
    @Test
    void signUp() throws Exception {

        // given
        doReturn(Mockito.mock(User.class))
                .when(loginService).signUp(any(SignUpRequest.class));
        // when
        // then
        SignUpRequest request = AbstractTest.getSignUpRequest("user", "user");
        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void signUp_duplicatedUsername() throws Exception {

        // given
        doThrow(new AlreadyExistException(AlreadyExistException.ID))
                .when(loginService).signUp(any(SignUpRequest.class));
        // when
        // then
        SignUpRequest request = AbstractTest.getSignUpRequest("user", "user");
        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkUsername() throws Exception {

        // given
        String username = "user";
        doReturn(true)
                .when(loginService).checkUsernameDuplication(username);
        // when
        // then
        MvcResult result = mockMvc.perform(get("/check-username").param("username", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"))
                .andReturn();
        System.out.println(result);
    }

    @Test
    void checkUsername_noParam() throws Exception {

        // given
        doCallRealMethod()
                .when(loginService).checkUsernameDuplication(anyString());
        // when
        // then
        mockMvc.perform(get("/check-username")
                .param("username", ""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void _checkUsername_noParam() throws Exception {

        // given
        // when
        // then
        mockMvc.perform(get("/check-username"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkNickname() throws Exception {

        // given
        String nickname = "user";
        doReturn(true)
                .when(loginService).checkNicknameDuplication(nickname);
        // when
        // then
        MvcResult result = mockMvc.perform(get("/check-nickname").param("nickname", nickname))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"))
                .andReturn();
        System.out.println(result);
    }

    @Test
    void verifyEmail() throws Exception {

        // given
        String email = "user@email.com";
        String token = "token";
        Tutee tutee = Mockito.mock(Tutee.class);
        doReturn(tutee)
                .when(loginService).verifyEmail(email, token);
        // when
        // then
        MvcResult result = mockMvc.perform(get("/verify-email")
                .param("email", email)
                .param("token", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result);
    }

    // TODO - CHECK
//    @Test
//    void verifyEmail_invalid() throws Exception {
//
//        // given
//        String email = "user";
//        String token = "token";
//        Tutee tutee = Mockito.mock(Tutee.class);
//        doReturn(tutee)
//                .when(loginService).verifyEmail(email, token);
//        // when
//        // then
//        mockMvc.perform(get("/verify-email")
//                .param("email", email)
//                .param("token", token))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void verifyEmail_notExistUser() throws Exception {

        // given
        String email = "user@email.com";
        String token = "token";
        doThrow(new RuntimeException("해당 계정의 미인증 사용자가 존재하지 않습니다."))
                .when(loginService).verifyEmail(email, token);
        // when
        // then
        mockMvc.perform(get("/verify-email")
                .param("email", email)
                .param("token", token))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    void verifyEmail_notVerified() throws Exception {

        // given
        String email = "user@email.com";
        String token = "token";
        doThrow(new RuntimeException("인증 실패"))
                .when(loginService).verifyEmail(email, token);
        // when
        // then
        mockMvc.perform(get("/verify-email")
                .param("email", email)
                .param("token", token))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    void login() throws Exception {

        // given
        LoginRequest loginRequest = LoginRequest.of(
                "user@email.com", "password"
        );
        Map result = mock(Map.class);
        when(result.get("token")).thenReturn("abcd");
        when(loginService.login(loginRequest)).thenReturn(result);

        // when
        // then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string("abcd"));
    }

    @Test
    void findPassword() throws Exception {

        // given
        String username = "user";
        doNothing()
                .when(loginService).findPassword(username);
        // when
        // then
        mockMvc.perform(get("/find-password")
                .param("username", username))
                .andDo(print())
                .andExpect(status().isOk());
    }
}