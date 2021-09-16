package com.tutor.tutorlab;

import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.PrincipalDetailsService;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
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
        if (userRepository.findByUsername(username) == null) {

            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .username(username)
                    .password("password")
                    .passwordConfirm("password")
                    .name(name)
                    .gender("MALE")
                    .phoneNumber(null)
                    .email(null)
                    .nickname(null)
                    .bio(null)
                    .zone(null)
                    .build();
            loginService.signUp(signUpRequest);
        }

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
