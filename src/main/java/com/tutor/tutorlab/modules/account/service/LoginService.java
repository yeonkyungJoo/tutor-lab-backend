package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleInfo;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleOAuth;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpOAuthDetailRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.GenderType;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final TutorRepository tutorRepository;
    private final TuteeRepository tuteeRepository;
    private final GoogleOAuth googleOAuth;

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

    public OAuthInfo getOAuthInfo(String provider, String code) throws Exception {

        Map<String, String> userInfo = null;
        OAuthInfo oAuthInfo = null;

        // convert
        OAuthType oAuthType = OAuthInfo.getOAuthType(provider);
        switch (oAuthType) {
            case GOOGLE:
                userInfo = googleOAuth.requestLogin(code);
                // System.out.println(userInfo);
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
                if (userInfo != null) {
                    oAuthInfo = new GoogleInfo(userInfo);
                }
                break;
            case NAVER:
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
