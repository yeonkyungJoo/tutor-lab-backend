package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.PrincipalDetailsService;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleInfo;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpOAuthDetailRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@MockMvcTest
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TuteeRepository tuteeRepository;

    @Autowired
    LoginService loginService;
    @Autowired
    PrincipalDetailsService principalDetailsService;
    @Autowired
    JwtTokenManager jwtTokenManager;

    // TODO - OAuth 테스트
    // @Test
    void oauth() {
    }

    @Test
    void signUp() throws Exception {

        // Given
        // When
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();

        mockMvc.perform(post("/sign-up")
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertNull(user);
        user = userRepository.findAllByUsername("yk@email.com");
        assertEquals(RoleType.TUTEE, user.getRole());
        assertEquals(GenderType.FEMALE, user.getGender());
        assertFalse(user.isEmailVerified());
        assertNull(user.getEmailVerifiedAt());

        Tutee tutee = tuteeRepository.findByUser(user);
        assertNull(tutee);
    }

    @Test
    @DisplayName("일반 회원가입 - Invalid Input")
    public void signUp_withInvalidInput() throws Exception {

        // Given
        // When
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk@email.com")
                .password("password")
                .passwordConfirm("passwordconfirm")
                .name("yk")
                .gender("FEMALE")
                .build();

        // Then
        mockMvc.perform(post("/sign-up")
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Invalid Input"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void verifyEmail() throws Exception {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        User user = loginService.signUp(signUpRequest);

        // When
        mockMvc.perform(get("/verify-email")
                .param("email", user.getUsername())
                .param("token", user.getEmailVerifyToken()))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertNotNull(user);
        assertTrue(user.isEmailVerified());
        assertNotNull(user.getEmailVerifiedAt());

        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);

    }

    @DisplayName("일반 로그인 후 jwtToken 확인")
    @Test
    void login() throws Exception {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        LoginRequest loginRequest = LoginRequest.builder()
                .username("yk@email.com")
                .password("password")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(loginRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(header().exists("Authorization"))
                .andReturn().getResponse();

        // Then
        assertTrue(response.getContentAsString().startsWith("Bearer"));
    }

    @Test
    void 로그인_실패() throws Exception {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        // Then
        LoginRequest loginRequest = LoginRequest.builder()
                .username("yk@email.com")
                .password("password_")
                .build();

        mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(loginRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHENTICATED.getCode()));
    }

    @Test
    void findPassword() {
    }

    @DisplayName("OAuth 회원가입 후 상세정보 저장")
    @Test
    void signUpOAuthDetail() throws Exception {

        // Given
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", "1234567890");
        userInfo.put("name", "yk");
        userInfo.put("email", "yk@email.com");
        loginService.signUpOAuth(new GoogleInfo(userInfo));

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername("yk@email.com");
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        SignUpOAuthDetailRequest signUpOAuthDetailRequest = SignUpOAuthDetailRequest.builder()
                .gender("FEMALE")
                .phoneNumber("010-1234-5678")
                .email("yk@email.com")
                .nickname("nickname")
                .bio("hello")
                .build();

        mockMvc.perform(post("/sign-up/oauth/detail")
                .content(objectMapper.writeValueAsString(signUpOAuthDetailRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertNotNull(user);
        assertEquals(OAuthType.GOOGLE, user.getProvider());
        assertEquals(RoleType.TUTEE, user.getRole());
        assertEquals(GenderType.FEMALE, user.getGender());
        assertEquals("nickname", user.getNickname());

        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);
    }

    @DisplayName("회원 정보 추가 입력 - OAuth 가입이 아닌 경우")
    @WithAccount("yk")
    @Test
    void signUpOAuthDetail_notOAuthUser() throws Exception {

        // Given
        // When
        // Then
        SignUpOAuthDetailRequest signUpOAuthDetailRequest = SignUpOAuthDetailRequest.builder()
                .gender("FEMALE")
                .phoneNumber("010-1234-5678")
                .email("yk@email.com")
                .nickname("nickname")
                .bio("hello")
                .build();

        mockMvc.perform(post("/sign-up/oauth/detail")
                .content(objectMapper.writeValueAsString(signUpOAuthDetailRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}