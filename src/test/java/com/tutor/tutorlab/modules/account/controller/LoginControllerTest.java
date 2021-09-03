package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.PrincipalDetailsService;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@MockMvcTest
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TuteeRepository tuteeRepository;
    @Autowired
    LoginService loginService;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtTokenManager jwtTokenManager;
    @Autowired
    PrincipalDetailsService principalDetailsService;

    @Test
    @DisplayName("일반 회원가입")
    public void signUp() throws Exception {

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
        User user = userRepository.findByName("yk");
        assertNotNull(user);
        assertEquals(GenderType.FEMALE, user.getGender());

        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);

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
                .andExpect(jsonPath("$.message").value("Invalid Input"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("OAuth 회원가입 - 구글")
    public void signUpOAuthGoogle() {

        // Given
        // When
        // Then

    }

    @Test
    @DisplayName("일반 로그인 후 jwtToken 확인")
    public void login() throws Exception {

        String name = "yk";
        String username = name + "@email.com";
        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username(username)
                .password("password")
                .passwordConfirm("password")
                .name(name)
                .gender("MALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        loginService.signUp(signUpRequest);

        // When
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password("password")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(loginRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse();

        // Then
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        assertNotNull(principalDetails);
        User user = principalDetails.getUser();
        assertNotNull(user);

        assertEquals("yk@email.com", user.getUsername());
        assertEquals(GenderType.MALE, user.getGender());
        assertEquals(RoleType.ROLE_TUTEE, user.getRole());

        assertEquals(1, principalDetails.getAuthorities().size());
        principalDetails.getAuthorities().stream()
                .forEach(grantedAuthority -> {
                    System.out.println(grantedAuthority.getAuthority());
                });
        */

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);
        String jwtToken = response.getHeader("Authorization").substring(7);
        /*
        DecodedJWT decodedJWT = jwtTokenManager.getDecodedToken(jwtToken);
        System.out.println("Claims : " + decodedJWT.getClaims());
        System.out.println("Algorithm : " + decodedJWT.getAlgorithm());
        System.out.println("Subject : " + decodedJWT.getSubject());
        System.out.println("IssuedAt : " + decodedJWT.getIssuedAt());
        System.out.println("ExpiresAt : " + decodedJWT.getExpiresAt());
        */
        assertTrue(jwtTokenManager.verifyToken(jwtToken, principalDetails));

    }

    @Test
    @DisplayName("OAuth 로그인 - 구글")
    public void loginOAuthGoogle() {

        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("OAuth 회원가입 후 상세정보 저장")
    public void signUpOAuthDetail() {

        // Given
        // When
        // Then
    }

}