package com.tutor.tutorlab.modules.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;

//    @Test
//    void processLoginOAuth() {
//    }

//    @Test
//    void oauth() {
//    }

    @DisplayName("OAuth 가입")
    @Test
    void signUpOAuth() {
    }

    @Test
    void loginOAuth() {
    }

    @Test
    void signUpOAuthDetail() {
    }

    @DisplayName("회원가입")
    @Test
    void signUp() {
    }

    @DisplayName("회원가입 - AlreadyExistException 발생")
    @Test
    void signUpWithExistingUsername() {

    }

    @Test
    void verifyEmail() {
    }

    @Test
    void login() {
    }

    @Test
    void findPassword() {
    }
}