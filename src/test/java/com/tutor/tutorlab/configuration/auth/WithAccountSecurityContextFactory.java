package com.tutor.tutorlab.configuration.auth;

import com.tutor.tutorlab.config.init.TestDataBuilder;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.PrincipalDetailsService;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    // private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    private final LoginService loginService;
    private final PrincipalDetailsService principalDetailsService;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {

        /*
            String name = withAccount.value();
            User user = User.builder()
                    .username(name + "@email.com")
                    .password(bCryptPasswordEncoder.encode("password"))
                    .name(name)
                    .gender("MALE")
                    .phoneNumber(null)
                    .email(null)
                    .nickname(null)
                    .bio(null)
                    .zone(null)
                    .role(RoleType.ROLE_TUTEE)
                    .provider(null)
                    .providerId(null)
                    .build();
            userRepository.save(user);
         */

        String name = withAccount.value();
        String username = name + "@email.com";
        if (!userRepository.findByUsername(username).isPresent()) {

            User user = loginService.signUp(TestDataBuilder.getSignUpRequest(name, "서울특별시 강남구 삼성동"));
            loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());
        }

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}