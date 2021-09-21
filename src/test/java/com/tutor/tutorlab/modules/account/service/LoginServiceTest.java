package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleInfo;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
class LoginServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    LoginService loginService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TuteeRepository tuteeRepository;

    @Transactional
    @DisplayName("User OAuth등록")
    @Test
    void signUpOAuth() {

        // Given
        Assertions.assertEquals(0, userRepository.count());
        Assertions.assertEquals(0, tuteeRepository.count());

        // When
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", "1234567890");
        userInfo.put("name", "test");
        userInfo.put("email", "test@email.com");

        GoogleInfo googleInfo = new GoogleInfo(userInfo);
        loginService.signUpOAuth(googleInfo);
        em.flush();

        // Then
//        System.out.println(userRepository.findAll());
//        System.out.println(tuteeRepository.findAll());
        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertEquals(1, tuteeRepository.count());
        Assertions.assertEquals(userRepository.findAll().get(0), tuteeRepository.findAll().get(0).getUser());
    }

    @Transactional
    @DisplayName("User 등록")
    @Test
    void signUp() {

        // Given
        Assertions.assertEquals(0, userRepository.count());
        Assertions.assertEquals(0, tuteeRepository.count());

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
        loginService.signUp(signUpRequest);
        em.flush();

        // Then
        System.out.println(userRepository.findAll());
        System.out.println(tuteeRepository.findAll());
    }

    @DisplayName("User 등록 - AlreadyExistException 발생")
    @WithAccount("yk")
    @Test
    void signUpWithExistingId() {

        // Given
        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertEquals(1, tuteeRepository.count());

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

        // Then
        Assertions.assertThrows(AlreadyExistException.class, () -> {
            loginService.signUp(signUpRequest);
        });
    }

    @Test
    public void random() {
        String random = RandomStringUtils.randomAlphanumeric(10);
        System.out.println(random);
    }
}