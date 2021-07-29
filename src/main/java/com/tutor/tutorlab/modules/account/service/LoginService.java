package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleInfo;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.kakao.KakaoInfo;
import com.tutor.tutorlab.config.security.oauth.provider.kakao.KakaoOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.kakao.KakaoResponse;
import com.tutor.tutorlab.config.security.oauth.provider.naver.NaverInfo;
import com.tutor.tutorlab.config.security.oauth.provider.naver.NaverOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.naver.NaverResponse;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpOAuthDetailRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.GenderType;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final TuteeRepository tuteeRepository;
    private final GoogleOAuth googleOAuth;
    private final KakaoOAuth kakaoOAuth;
    private final NaverOAuth naverOAuth;

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenManager jwtTokenManager;

    private boolean checkUsernameDuplication(String username) {
        boolean duplicated = false;

        User user = userRepository.findByUsername(username);
        if (user != null) {
            duplicated = true;
        }
        return duplicated;
    }

    // TODO - 리팩토링
    public OAuthInfo getOAuthInfo(String provider, String code) throws Exception {

        OAuthInfo oAuthInfo = null;

        // convert
        OAuthType oAuthType = OAuthInfo.getOAuthType(provider);
        switch (oAuthType) {
            case GOOGLE:
                Map<String, String> googleOAuthUserInfo = googleOAuth.getUserInfo(code);
                // System.out.println(googleOAuthUserInfo);
                /*
                {
                    id=109497631191479413556,
                    email=dev.yk2021@gmail.com,
                    verified_email=true,
                    name=yk dev,
                    given_name=yk,
                    family_name=dev,
                    picture=https://lh3.googleusercontent.com/a/AATXAJwH1OzOPyWFMo8BgTGs_JYle0po58A7PxaRj-0X=s96-c,
                    locale=ko
                }
                */
                if (googleOAuthUserInfo != null) {
                    oAuthInfo = new GoogleInfo(googleOAuthUserInfo);
                }
                break;
            case KAKAO:
                KakaoResponse kakaoOAuthUserInfo = kakaoOAuth.getUserInfo(code);
                // System.out.println(kakaoOAuthUserInfo);
                /*
                    {
                        id=1825918761,
                        connected_at=2021-07-28T21:58:30Z,
                        properties={
                            nickname=dev.yk
                        },
                        kakao_account={
                            profile_nickname_needs_agreement=false,
                            profile={
                                nickname=dev.yk
                            },
                            has_email=true,
                            email_needs_agreement=false,
                            is_email_valid=true,
                            is_email_verified=true,
                            email=dev.yk2021@gmail.com
                        }
                    }
                */
                if (kakaoOAuthUserInfo != null) {
                    oAuthInfo = new KakaoInfo(kakaoOAuthUserInfo);
                }
                break;
            case NAVER:
                NaverResponse naverOAuthUserInfo = naverOAuth.getUserInfo(code);
                // System.out.println(naverOAuthUserInfo);

                if (naverOAuthUserInfo != null) {
                    oAuthInfo = new NaverInfo(naverOAuthUserInfo);
                    // System.out.println(oAuthInfo);
                }
                break;
            default:
                break;
        }


        return oAuthInfo;
    }

    public Map<String, String> signUpOAuth(OAuthInfo oAuthInfo) throws Exception {

        String username = oAuthInfo.getEmail();
        if (checkUsernameDuplication(username)) {
            // TODO - 에러
            return null;
        }

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(username))
                .name(oAuthInfo.getName())
                .gender(null)
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .role(RoleType.ROLE_TUTEE)
                .provider(oAuthInfo.getProvider())
                .providerId(oAuthInfo.getProviderId())
                .build();

        userRepository.save(user);
        // 강제 로그인
        return loginOAuth(user);
    }

    public Map<String, String> loginOAuth(User user) throws Exception {

        try {
            String username = user.getUsername();
            return login(username, username);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO - 에러
        }

        return null;
    }

    public void signUpOAuthDetail(User user, SignUpOAuthDetailRequest signUpOAuthDetailRequest) {

        // TODO - CHECK : 영속성 컨텍스트
        user = userRepository.findByUsername(user.getUsername());

        user.setGender(signUpOAuthDetailRequest.getGender() == "MALE" ? GenderType.MALE : GenderType.FEMALE);
        user.setPhoneNumber(signUpOAuthDetailRequest.getPhoneNumber());
        user.setEmail(signUpOAuthDetailRequest.getEmail());
        user.setNickname(signUpOAuthDetailRequest.getNickname());
        user.setBio(signUpOAuthDetailRequest.getBio());
        user.setZone(signUpOAuthDetailRequest.getZone());
    }

    public Tutee signUp(SignUpRequest signUpRequest) {

        String username = signUpRequest.getUsername();
        if (checkUsernameDuplication(username)) {
            // TODO - 에러
            return null;
        }

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
                .gender(signUpRequest.getGender())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .email(signUpRequest.getEmail())
                .nickname(signUpRequest.getNickname())
                .bio(signUpRequest.getBio())
                .zone(signUpRequest.getZone())
                .role(RoleType.ROLE_TUTEE)
                .provider(null)
                .providerId(null)
                .build();

        userRepository.save(user);

        Tutee tutee = new Tutee();
        tutee.setUser(user);
        return tuteeRepository.save(tutee);
    }

    private Authentication authenticate(String username, String password) throws Exception {

        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // TODO - CHECK
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;

        } catch(BadCredentialsException e) {
            // TODO - error message
        } catch(DisabledException e) {
            // TODO - error message
        } catch(LockedException e) {
            // TODO - error message
        }
        return null;
    }

    public Map<String, String> login(String username, String password) throws Exception {

        Authentication authentication = authenticate(username, password);
        if (authentication != null) {

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            String jwtToken = jwtTokenManager.createToken(principalDetails.getUsername(), claims);
            return jwtTokenManager.convertTokenToMap(jwtToken);
        }

        return null;
    }

    public Map<String, String> login(LoginRequest request) throws Exception {
        return this.login(request.getUsername(), request.getPassword());
    }

}
