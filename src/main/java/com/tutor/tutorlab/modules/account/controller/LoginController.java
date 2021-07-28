package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpOAuthDetailRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.vo.User;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final UserRepository userRepository;
    // private final SignUpRequestValidator signUpRequestValidator;

/*    @InitBinder("signUpRequest")
    public void validateSignUpRequest(WebDataBinder binder) {
        binder.addValidators(signUpRequestValidator);
    }*/

    @GetMapping("/oauth/{provider}")
    public void oauth(@PathVariable(name = "provider") String provider, HttpServletResponse response) {
        
        try {
            // 로그인 요청 주소
            // 사용자가 동의하면 code를 callback
            String url = null;
            if (provider.equals("google")) {
                url = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&redirect_uri=http://localhost:8080/oauth/google/callback&client_id=902783645965-ald60d1ehnaeaoetihtb1861u98ppf3u.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
            } else if (provider.equals("kakao")) {
                url = "https://kauth.kakao.com/oauth/authorize?client_id=8dc9eea7e202a581e0449058e753beaf&redirect_uri=http://localhost:8080/oauth/kakao/callback&response_type=code";
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
     */
    @GetMapping("/oauth/{provider}/callback")
    public ResponseEntity oauth(@PathVariable(name = "provider") String provider,
            @RequestParam(name = "code") String code) {

        try {
            if (!StringUtils.hasLength(code)) {
                // TODO - 예외 처리
            }

            OAuthInfo oAuthInfo = loginService.getOAuthInfo(provider, code);
            if (oAuthInfo != null) {

                User user = userRepository.findByProviderAndProviderId(oAuthInfo.getProvider(), oAuthInfo.getProviderId());
                if (user != null) {
                    // 이미 가입된 회원이므로 바로 로그인 진행
                    Map<String, String> result = loginService.loginOAuth(user);

                    // TODO - TOKEN
                    // System.out.println(result);
                    // {header=Authorization, token=Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZXYueWsyMDIxQGdtYWlsLmNvbSIsImV4cCI6MTYyNjYxMzg5NCwiaWF0IjoxNjI2NTI3NDk0fQ.j_d2B_Gsl1XVNDAYuMeYO_3DGznH_UOzGIL2J7Y3Yas}

                    return null;
                } else {

                    // 회원가입 - 강제 로그인 
                    // 추가 정보 입력 필요
                    Map<String, String> result = loginService.signUpOAuth(oAuthInfo);

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
    @ApiOperation("OAuth 회원가입 추가 정보 입력")
    @PostMapping("/sign-up/oauth/detail")
    public ResponseEntity signUpOAuthDetail(@CurrentUser User user,
                                            @RequestBody SignUpOAuthDetailRequest signUpOAuthDetailRequest) {

        if (user == null) {

        }

        loginService.signUpOAuthDetail(user, signUpOAuthDetailRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 일반 회원가입 - 기본 튜티로 가입
     */
    @ApiOperation("일반 회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest) {

        loginService.signUp(signUpRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 일반 로그인
     */
    @ApiOperation("일반 로그인")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {

        try {
            Map<String, String> result = loginService.login(request);

            // TODO - CHECK
            /*
            	public ResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
                    this(null, headers, status);
                }

                public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
                    this(body, headers, (Object) status);
                }
            */
            return new ResponseEntity(getHeaders(result), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO - 예외 처리 및 ErrorResponse 반환
        return null;
    }

    private HttpHeaders getHeaders(Map<String, String> result) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(result.get("header"), result.get("token"));
        return headers;
    }

}
