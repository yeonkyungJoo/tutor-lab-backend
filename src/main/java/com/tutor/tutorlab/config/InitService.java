package com.tutor.tutorlab.config;

import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitService {

    private final LoginService loginService;
    private final UserRepository userRepository;


    @PostConstruct
    @Transactional
    void init() {

        if (userRepository.count() != 0) {
            return;
        }

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

    }

}
