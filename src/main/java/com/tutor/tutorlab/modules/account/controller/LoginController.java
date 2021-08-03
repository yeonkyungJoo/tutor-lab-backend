package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpOAuthDetailRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"LoginController"})
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

    // 네이버 OAuth 로그인 필수 파라미터
    // https://developers.naver.com/docs/login/web/web.md
    // CSRF 방지를 위한 상태 토큰 생성
    // 추후 검증을 위해 세션에 저장된다.
    private String generateState() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @GetMapping("/oauth/{provider}")
    public void oauth(@PathVariable(name = "provider") String provider, HttpServletRequest request, HttpServletResponse response) {
        
        try {
            // 로그인 요청 주소
            // 사용자가 동의하면 code를 callback
            String url = null;
            if (provider.equals("google")) {
                url = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code&redirect_uri=http://localhost:8080/oauth/google/callback&client_id=902783645965-ald60d1ehnaeaoetihtb1861u98ppf3u.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
            } else if (provider.equals("kakao")) {
                url = "https://kauth.kakao.com/oauth/authorize?client_id=8dc9eea7e202a581e0449058e753beaf&redirect_uri=http://localhost:8080/oauth/kakao/callback&response_type=code";
            } else if (provider.equals("naver")) {

                String state = generateState();
                request.getSession().setAttribute("state", state);
                url = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="
                        + "NNG0ZvRBJlxlE5DbApJR" + "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/oauth/naver/callback", "UTF-8") + "&state=" + state;
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
        // 네이버 - state, error, error_description

        try {
            if (!StringUtils.hasLength(code)) {
                // TODO - 예외 처리
            }

            OAuthInfo oAuthInfo = loginService.getOAuthInfo(provider, code);
            if (oAuthInfo != null) {

                User user = userRepository.findByProviderAndProviderId(oAuthInfo.getProvider(), oAuthInfo.getProviderId());

                Map<String, String> result = null;
                if (user != null) {
                    // 이미 가입된 회원이므로 바로 로그인 진행
                    result = loginService.loginOAuth(user);
                } else {
                    // 회원가입 - 강제 로그인
                    // 추가 정보 입력 필요
                    result = loginService.signUpOAuth(oAuthInfo);
                }

                return new ResponseEntity(getHeaders(result), HttpStatus.OK);

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
