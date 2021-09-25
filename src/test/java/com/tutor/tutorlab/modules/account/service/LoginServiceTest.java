package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleInfo;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpOAuthDetailRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TuteeRepository tuteeRepository;

    @Autowired
    JwtTokenManager jwtTokenManager;

//    @Test
//    void processLoginOAuth() {
//    }

//    @Test
//    void oauth() {
//    }

    @Test
    void 회원가입_OAuth() {

        // Given
        // When
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", "1234567890");
        userInfo.put("name", "test");
        userInfo.put("email", "test@email.com");

        GoogleInfo googleInfo = new GoogleInfo(userInfo);
        Map<String, String> result = loginService.signUpOAuth(googleInfo);

        // Then
        // 유저 생성 확인
        // 이메일 verify 확인
        User user = userRepository.findByUsername("test@email.com").orElse(null);
        assertNotNull(user);
        assertTrue(user.isEmailVerified());
        System.out.println(String.format("provider : %s, providerId : %s", user.getProvider(), user.getProviderId()));
        // 튜티 생성 확인
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);
        // 로그인 확인 - jwt 토큰생성
        assertTrue(result.containsKey("header"));
        assertTrue(result.containsKey("token"));

    }

//    @Test
//    void loginOAuth() {
//    }

    @Test
    void signUpOAuthDetail() {

        // Given
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", "1234567890");
        userInfo.put("name", "test");
        userInfo.put("email", "test@email.com");
        loginService.signUpOAuth(new GoogleInfo(userInfo));

        // When
        User user = userRepository.findByUsername("test@email.com").orElse(null);
        SignUpOAuthDetailRequest signUpOAuthDetailRequest = SignUpOAuthDetailRequest.builder()
                .gender("FEMALE")
                .phoneNumber("010-1234-5678")
                .email("test@email.com")
                .nickname("nickname")
                .bio("hello")
                .build();
        loginService.signUpOAuthDetail(user, signUpOAuthDetailRequest);

        // Then
        user = userRepository.findByUsername("test@email.com").orElse(null);
        assertTrue(user.isEmailVerified());
        assertEquals("nickname", user.getNickname());
    }

    @Test
    void 회원가입() {

        // Given
        // When
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        loginService.signUp(signUpRequest);

        // Then
        User user = userRepository.findByUsername("test@email.com").orElse(null);
        assertNull(user);
        user = userRepository.findAllByUsername("test@email.com");
        assertNotNull(user);
        assertFalse(user.isEmailVerified());
        assertEquals(RoleType.TUTEE, user.getRole());
        assertEquals(GenderType.FEMALE, user.getGender());

        Tutee tutee = tuteeRepository.findByUser(user);
        assertNull(tutee);
    }

    @DisplayName("회원가입 - AlreadyExistException 발생")
    @Test
    void signUpWithExistingUsername() {

        // Given
        SignUpRequest _signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        loginService.signUp(_signUpRequest);
        assertNotNull(userRepository.findAllByUsername("test@email.com"));

        // When
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
                .gender("MALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        assertThrows(AlreadyExistException.class, () -> {
            loginService.signUp(signUpRequest);
        });
    }

    @Test
    void verifyEmail() {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        User user = loginService.signUp(signUpRequest);
        assertFalse(user.isEmailVerified());
        assertNull(userRepository.findByUsername("test@email.com").orElse(null));
        assertNotNull(userRepository.findAllByUsername("test@email.com"));

        // When
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // Then
        user = userRepository.findByUsername("test@email.com").orElse(null);
        assertNotNull(user);
        assertTrue(user.isEmailVerified());
    }

    @DisplayName("회원 정보 추가 입력 - OAuth 가입이 아닌 경우")
    @Test
    void signUpOAuthDetail_notOAuthUser() {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
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
        User verifiedUser = userRepository.findByUsername("test@email.com").orElse(null);
        SignUpOAuthDetailRequest signUpOAuthDetailRequest = SignUpOAuthDetailRequest.builder()
                .gender("FEMALE")
                .phoneNumber("010-1234-5678")
                .email("test@email.com")
                .nickname("nickname")
                .bio("hello")
                .build();

        assertThrows(RuntimeException.class, () -> {
            loginService.signUpOAuthDetail(verifiedUser, signUpOAuthDetailRequest);
        });
    }

    @Test
    void login() {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
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
                .username("test@email.com")
                .password("password")
                .build();
        Map<String, String> result = loginService.login(loginRequest);

        // Then
        assertTrue(result.containsKey("header"));
        assertTrue(result.containsKey("token"));

        String jwtToken = result.get("token").replace("Bearer ", "");
        assertEquals(jwtTokenManager.getClaim(jwtToken, "username"), "test@email.com");
    }

    @Test
    void login_unverifiedUser() {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        User user = loginService.signUp(signUpRequest);

        // When
        // Then
        LoginRequest loginRequest = LoginRequest.builder()
                .username("test@email.com")
                .password("password")
                .build();

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void login_wrongPassword() {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("test@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("test")
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
                .username("test@email.com")
                .password("wrong")
                .build();

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void findPassword() {
    }
}