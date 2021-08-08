package com.tutor.tutorlab.config;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitService {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    @Transactional
    void init() {

/*        if (userRepository.count() != 0) {
            return;
        }*/

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


        // OAuth 회원가입
        String username = "sj@email.com";
        if (userRepository.findByUsername(username) == null) {
            /*
            User user = User.builder()
                    .username(username)
                    .password(bCryptPasswordEncoder.encode(username))
                    .name("sj")
                    .gender(null)
                    .phoneNumber(null)
                    .email(null)
                    .nickname(null)
                    .bio(null)
                    .zone(null)
                    .role(RoleType.ROLE_TUTEE)
                    .provider(OAuthType.GOOGLE)
                    .providerId("google1")
                    .build();

            userRepository.save(user);
            */

            OAuthInfo oAuthInfo = new OAuthInfo() {
                @Override
                public String getProviderId() {
                    return "google1";
                }

                @Override
                public OAuthType getProvider() {
                    return OAuthType.GOOGLE;
                }

                @Override
                public String getName() {
                    return "sj";
                }

                @Override
                public String getEmail() {
                    return "sj@email.com";
                }
            };

            try {
                loginService.signUpOAuth(oAuthInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
