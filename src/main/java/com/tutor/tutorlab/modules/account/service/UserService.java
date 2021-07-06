package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.OAuthLoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.OAuthSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUp(SignUpRequest request) {

    }

    private void authenticate(String username, String password) throws Exception {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch(BadCredentialsException e) {

        } catch(DisabledException e) {

        } catch(LockedException e) {

        }

    }

    public void login(LoginRequest request) throws Exception {
        authenticate(request.getUsername(), request.getPassword());

        // jwtToken 생성

    }

    public void signUpOAuth(OAuthSignUpRequest request) {
    }

    public void loginOAuth(OAuthLoginRequest request) {
    }
}
