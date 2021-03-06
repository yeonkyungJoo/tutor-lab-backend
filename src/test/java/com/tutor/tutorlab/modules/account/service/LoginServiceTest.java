package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.kakao.KakaoOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.naver.NaverOAuth;
import com.tutor.tutorlab.mail.EmailMessage;
import com.tutor.tutorlab.mail.EmailService;
import com.tutor.tutorlab.mail.HtmlEmailService;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    LoginService loginService;

    @Mock
    UserRepository userRepository;
    @Mock
    TuteeRepository tuteeRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    JwtTokenManager jwtTokenManager;
    @Mock
    AuthenticationManager authenticationManager;

    @Spy
    EmailService emailService;
    @Spy
    TemplateEngine templateEngine = new SpringTemplateEngine();

    @BeforeEach
    void setup() {
        assertNotNull(loginService);
    }

    @Test
    void checkUsernameDuplication() {
        // username

        // given
        String username = "user1@email.com";
        User user = Mockito.mock(User.class);
        when(userRepository.findAllByUsername(username)).thenReturn(user);

        // when
        boolean result = loginService.checkUsernameDuplication(username);
        // then
        assertTrue(result);
    }

    @Test
    void checkUsernameDuplication_withNoParam() {
        // username

        // given
        // when
        // then
        assertThrows(IllegalArgumentException.class,
                () -> loginService.checkUsernameDuplication(""));
    }

    @Test
    void checkUsernameDuplication_duplicated() {
        // username

        // given
        String username = "user1@email.com";
        when(userRepository.findAllByUsername(username)).thenReturn(null);

        // when
        boolean result = loginService.checkUsernameDuplication(username);
        // then
        assertFalse(result);
    }

    @Test
    void checkNicknameDuplication() {
        // nickname

        // given
        String nickname = "user1";
        User user = Mockito.mock(User.class);
        when(userRepository.findAllByNickname(nickname)).thenReturn(user);

        // when
        boolean result = loginService.checkNicknameDuplication(nickname);
        // then
        assertTrue(result);
    }

    @Test
    void checkNicknameDuplication_duplicated() {
        // nickname

        // given
        String nickname = "user1";
        User user = Mockito.mock(User.class);
        when(userRepository.findAllByNickname(nickname)).thenReturn(null);

        // when
        boolean result = loginService.checkNicknameDuplication(nickname);
        // then
        assertFalse(result);
    }

    @Test
    void signUp_checkSendEmail() {
        // signUpRequest

        // given
        when(userRepository.findAllByUsername(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        // when
        String username = "user1@email.com";
        SignUpRequest signUpRequest = SignUpRequest.of(
                username,
                "password",
                "password",
                "user1",
                "FEMALE",
                null,
                null,
                null,
                "user1",
                null,
                "??????????????? ????????? ?????????",
                null
        );
        loginService.signUp(signUpRequest);

        // then
        // this error might show up because you verify either of: final/private/equals()/hashCode() methods
        // verify(templateEngine, atLeastOnce()).process(anyString(), new Context());
        verify(emailService, atLeastOnce()).send(any(EmailMessage.class));
    }

    @Test
    void signUp_existUsername() {
        // signUpRequest

        // given
        String username = "user1@email.com";
        when(userRepository.findAllByUsername(username)).thenReturn(Mockito.mock(User.class));

        // when
        // then
        SignUpRequest signUpRequest = SignUpRequest.of(
                username,
                "password",
                "password",
                "user1",
                "FEMALE",
                null,
                null,
                null,
                "user1",
                null,
                "??????????????? ????????? ?????????",
                null
        );

        assertThrows(AlreadyExistException.class,
                () -> loginService.signUp(signUpRequest));
    }

    @Test
    void verifyEmail() {
        // email(username), token

        // given
        String email = "user1@email.com";

        User user = User.of(email, null, null, null, null, null,
                email, null, null, null, null, null, null, null);
        user.generateEmailVerifyToken();
        assert !user.isEmailVerified();
        when(userRepository.findUnverifiedUserByUsername(email)).thenReturn(Optional.of(user));

        // when
        loginService.verifyEmail(email, user.getEmailVerifyToken());

        // then
        assertTrue(user.isEmailVerified());
    }

    @DisplayName("???????????? ?????? ?????????")
    @Test
    void verifyEmail_notExistUser() {

        // given
        String email = "user1@email.com";
        String token = "token";
        when(userRepository.findUnverifiedUserByUsername(email)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(RuntimeException.class,
                () -> loginService.verifyEmail(email, token));

    }

    @DisplayName("?????? ????????? ?????????")
    @Test
    void verifyEmail_alreadyVerifiedUser() {

        // given
        String email = "user1@email.com";

        User user = User.of(email, null, null, null, null, null,
                email, null, null, null, null, null, null, null);
        user.generateEmailVerifyToken();

        user.verifyEmail();
        when(userRepository.findUnverifiedUserByUsername(email)).thenReturn(Optional.of(user));

        // when
        // then
        assertThrows(RuntimeException.class,
                () -> loginService.verifyEmail(email, user.getEmailVerifyToken())
        );

    }


    @Test
    void findPassword() {
        // ?????? ???????????? ?????? ??? ????????? ??????
        // username

        // given
        String username = "user1@email.com";
        String password = "password";
        String randomPassword = "randomPassword";
        User user = User.of(username, password, null, null, null, null,
                null, null, null, null, null, null, null, null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(randomPassword);
        // when
        loginService.findPassword(username);

        // then
        // 1. ?????? ???????????? ?????? - passwordEncoder
        verify(bCryptPasswordEncoder, atLeastOnce()).encode(anyString());
        // 2. user??? set
        // 3. ????????? ??????
        verify(emailService, atLeastOnce()).send(any(EmailMessage.class));
    }

    // TODO - ????????? ?????????
//    @Test
//    void login() {
//        // username, password
//
//        // given
//
//        // when
//        // then
//    }
}