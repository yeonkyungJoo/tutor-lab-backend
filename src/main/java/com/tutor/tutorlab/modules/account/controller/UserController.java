package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.TuteeSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.UserService;
import com.tutor.tutorlab.modules.account.validator.SignUpRequestValidator;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final SignUpRequestValidator signUpRequestValidator;

    @InitBinder("signUpRequest")
    public void validateSignUpRequest(WebDataBinder binder) {
        binder.addValidators(signUpRequestValidator);
    }

    @GetMapping("/oauth/{provider}")
    public void oauth(@PathVariable(name = "provider") String provider, HttpServletResponse response) {

        try {
            String url = null;
            if (provider.equals("google")) {
                url = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&redirect_uri=http://localhost:8080/oauth/google/callback&client_id=902783645965-ald60d1ehnaeaoetihtb1861u98ppf3u.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
            }

            if (StringUtils.hasLength(url)) {
                response.sendRedirect(url);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     OAuth 로그인/회원가입
     - google : https://accounts.google.com/o/oauth2/v2/auth?response_type=code&redirect_uri=http://localhost:8080/oauth/google/callback&client_id=902783645965-ald60d1ehnaeaoetihtb1861u98ppf3u.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile
     */
    @GetMapping("/oauth/{provider}/callback")
    public ResponseEntity oauth(@PathVariable(name = "provider") String provider,
            @RequestParam(name = "code") String code) {

        try {
            OAuthInfo oAuthInfo = userService.getOAuthInfo(provider, code);
            if (oAuthInfo != null) {

                User user = userRepository.findByProviderAndProviderId(oAuthInfo.getProvider(), oAuthInfo.getProviderId());
                if (user != null) {
                    Map<String, String> result = userService.loginOAuth(user);
                    System.out.println(result);
                    // {header=Authorization, token=Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZXYueWsyMDIxQGdtYWlsLmNvbSIsImV4cCI6MTYyNjYxMzg5NCwiaWF0IjoxNjI2NTI3NDk0fQ.j_d2B_Gsl1XVNDAYuMeYO_3DGznH_UOzGIL2J7Y3Yas}

                    return null;
                } else {
                    Long userId = userService.signUpOAuth(oAuthInfo);
                    return null;
                }

            } else {
                // TODO
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * OAuth 회원가입 추가 정보 입력
     */
    @PostMapping("/sign-up/oauth/detail")
    public ResponseEntity signUpOAuthDetail() {

        return null;
    }

    /**
     * 일반 회원가입 - 튜터
     */
    @PostMapping("/sign-up/tutor")
    public ResponseEntity signUpTutor(@RequestBody TutorSignUpRequest tutorSignUpRequest) {
        userService.signUpTutor(tutorSignUpRequest);
        return null;
    }

    /**
     * 일반 회원가입 - 튜티
     */
    @PostMapping("/sign-up/tutee")
    public ResponseEntity signUpTutee(@RequestBody TuteeSignUpRequest tuteeSignUpRequest) {
        userService.signUpTutee(tuteeSignUpRequest);
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

}
