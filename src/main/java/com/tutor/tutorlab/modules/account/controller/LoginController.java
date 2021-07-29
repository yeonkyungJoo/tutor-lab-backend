package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TuteeSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
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
public class LoginController {

    private final LoginService loginService;
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
            OAuthInfo oAuthInfo = loginService.getOAuthInfo(provider, code);
            if (oAuthInfo != null) {

                User user = userRepository.findByProviderAndProviderId(oAuthInfo.getProvider(), oAuthInfo.getProviderId());
                if (user != null) {
                    Map<String, String> result = loginService.loginOAuth(user);
                    System.out.println(result);
                    // {header=Authorization, token=Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZXYueWsyMDIxQGdtYWlsLmNvbSIsImV4cCI6MTYyNjYxMzg5NCwiaWF0IjoxNjI2NTI3NDk0fQ.j_d2B_Gsl1XVNDAYuMeYO_3DGznH_UOzGIL2J7Y3Yas}

                    return null;
                } else {
                    Long userId = loginService.signUpOAuth(oAuthInfo);
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


    // 기본 튜티로 가입
    // 일반 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest) {
        loginService.signUp(signUpRequest);
        return null;
    }

    /**
     * 튜터로 가입
     */
    @PostMapping("/sign-up/tutor")
    public ResponseEntity signUpTutor(@RequestBody TutorSignUpRequest tutorSignUpRequest) {
        // TODO - @CurrentUser
        loginService.signUpTutor(tutorSignUpRequest);
        return null;
    }

    /**
     * 일반 로그인
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {

        try {
            loginService.login(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
