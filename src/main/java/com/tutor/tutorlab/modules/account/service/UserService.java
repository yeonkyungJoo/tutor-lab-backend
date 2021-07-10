package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
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

import java.util.HashMap;
import java.util.Map;


@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenManager jwtTokenManager;

    public void signUp(SignUpRequest request) {

        // check duplication


    }

    public void signUpOAuth(OAuthSignUpRequest request) {

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

    public Map<String, String> login(LoginRequest request) throws Exception {
        authenticate(request.getUsername(), request.getPassword());

        String jwtToken = jwtTokenManager.createToken(request.getUsername(), new HashMap<>());
        return jwtTokenManager.convertTokenToMap(jwtToken);
    }

    public void loginOAuth(OAuthLoginRequest request) {
    }
}
