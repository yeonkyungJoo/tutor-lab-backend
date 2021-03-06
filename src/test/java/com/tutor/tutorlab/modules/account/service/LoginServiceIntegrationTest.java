package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleInfo;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@Transactional
@SpringBootTest
class LoginServiceIntegrationTest extends AbstractTest {

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
        Map<String, String> result = loginService.signUpOAuth(new GoogleInfo(userInfo));

        // Then
        // 유저 생성 확인
        // 이메일 verify 확인
        User user = userRepository.findByUsername(USERNAME).orElse(null);
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
        loginService.signUpOAuth(new GoogleInfo(userInfo));

        // When
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        loginService.signUpOAuthDetail(user, signUpOAuthDetailRequest);

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        assertTrue(user.isEmailVerified());
        assertEquals(signUpOAuthDetailRequest.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    void 회원가입() {

        // Given
        // When
        loginService.signUp(signUpRequest);

        // Then
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        assertNull(user);

        User unverifiedUser = userRepository.findAllByUsername(USERNAME);
        assertAll(
                () -> assertNotNull(unverifiedUser),
                () -> assertFalse(unverifiedUser.isEmailVerified()),
                () -> assertEquals(RoleType.TUTEE, unverifiedUser.getRole()),
                () -> assertEquals(signUpRequest.getZone(), unverifiedUser.getZone().toString()),
                () -> assertEquals(signUpRequest.getPhoneNumber(), unverifiedUser.getPhoneNumber())
        );

        Tutee tutee = tuteeRepository.findByUser(user);
        assertNull(tutee);
    }

    @DisplayName("회원가입 - AlreadyExistException 발생")
    @Test
    void signUpWithExistingUsername() {

        // Given
        loginService.signUp(signUpRequest);
        assertNotNull(userRepository.findAllByUsername(USERNAME));

        // When
        assertThrows(AlreadyExistException.class, () -> {
            loginService.signUp(signUpRequest);
        });
    }

    @Test
    void verifyEmail() {

        // Given
        User user = loginService.signUp(signUpRequest);
        assertFalse(user.isEmailVerified());
        assertFalse(userRepository.findByUsername(USERNAME).isPresent());
        assertNotNull(userRepository.findAllByUsername(USERNAME));

        // When
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        assertNotNull(user);
        assertTrue(user.isEmailVerified());
    }

    @DisplayName("회원 정보 추가 입력 - OAuth 가입이 아닌 경우")
    @Test
    void signUpOAuthDetail_notOAuthUser() {

        // Given
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        // Then
        User verifiedUser = userRepository.findByUsername(USERNAME).orElse(null);
        assertThrows(RuntimeException.class, () -> {
            loginService.signUpOAuthDetail(verifiedUser, signUpOAuthDetailRequest);
        });
    }

    @Test
    void login() {

        // Given
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        Map<String, String> result = loginService.login(loginRequest);

        // Then
        assertTrue(result.containsKey("header"));
        assertTrue(result.containsKey("token"));

        String jwtToken = result.get("token").replace("Bearer ", "");
        assertEquals(USERNAME, jwtTokenManager.getClaim(jwtToken, "username"));
    }

    @Test
    void login_unverifiedUser() {

        // Given
        loginService.signUp(signUpRequest);

        // When
        // Then
        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void login_wrongPassword() {

        // Given
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        // Then
        loginRequest.setPassword("password_");
        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }

//    @Test
//    void findPassword() {
//    }
}