package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleInfo;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.modules.account.controller.request.*;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {

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

    private User createUser(SignUpRequest request) {

        String username = request.getUsername();
        if (checkUsernameDuplication(username)) {
            // TODO - 에러
            return null;
        }

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .nickname(request.getNickname())
                .bio(request.getBio())
                .zone(request.getZone())
                //.role(request.getRole())
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .provider(null)
                .providerId(null)
                .build();

        return userRepository.save(user);
    }

    public Long signUpTutor(TutorSignUpRequest tutorSignUpRequest) {

        // TODO - Tutor builder
        Tutor tutor = new Tutor();
        tutor.setUser(createUser(tutorSignUpRequest));
        tutor.setCreatedAt(LocalDateTime.now());

        return tutorRepository.save(tutor).getId();
    }

    public Long signUpTutee(TuteeSignUpRequest tuteeSignUpRequest) {

        // TODO - Tutee builder
        Tutee tutee = new Tutee();
        tutee.setUser(createUser(tuteeSignUpRequest));
        tutee.setCreatedAt(LocalDateTime.now());

        return tuteeRepository.save(tutee).getId();
    }

    public OAuthInfo getOAuthInfo(String provider, String code) throws Exception {

        Map<String, String> userInfo = null;
        OAuthInfo oAuthInfo = null;

        // convert
        OAuthType oAuthType = OAuthInfo.getOAuthType(provider);
        switch (oAuthType) {
            case GOOGLE:
                userInfo = googleOAuth.requestLogin(code);
                if (userInfo != null) {
                    oAuthInfo = new GoogleInfo(userInfo);
                }
                break;
            case NAVER:
                break;
            case KAKAO:
                break;
            case GITHUB:
                break;
            default:
                break;
        }
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

        return oAuthInfo;
    }

    public Long signUpOAuth(OAuthInfo oAuthInfo) {

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
                //.role(request.getRole())
                .role("ROLE_USER")
                .createdAt(LocalDateTime.now())
                .provider(oAuthInfo.getProvider())
                .providerId(oAuthInfo.getProviderId())
                .build();

        userRepository.save(user);
        return user.getId();
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

    private Authentication authenticate(String username, String password) throws Exception {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
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
