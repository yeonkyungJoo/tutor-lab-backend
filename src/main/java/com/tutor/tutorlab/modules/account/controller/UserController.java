package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.OAuthLoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.OAuthSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 일반 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request);
        return null;
    }

    /**
     * 일반 로그인
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {

        try {
            userService.login(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * OAuth 회원가입
     */
    @PostMapping("/sign-up/oauth")
    public ResponseEntity signUpOAuth(@RequestBody OAuthSignUpRequest request) {

        userService.signUpOAuth(request);
        return null;
    }

    /**
     * OAuth 로그인
     */
    @PostMapping("/login/oauth")
    public ResponseEntity loginOAuth(@RequestBody OAuthLoginRequest request) {

        userService.loginOAuth(request);
        return null;
    }

}
