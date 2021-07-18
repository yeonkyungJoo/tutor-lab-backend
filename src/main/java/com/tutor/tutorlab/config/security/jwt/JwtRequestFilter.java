package com.tutor.tutorlab.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    String secret;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = jwtHeader.replace("Bearer ", "");

        // TODO - verify token
        String username = JWT.require(Algorithm.HMAC256(secret)).build().verify(jwtToken).getClaim("username").asString();
        // System.out.println(username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository.findByUsername(username);
            if (user != null) {

                PrincipalDetails principalDetails = new PrincipalDetails(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

    }
}
