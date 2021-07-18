package com.tutor.tutorlab.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenManager {

    public JwtTokenManager(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.token-validity-in-seconds}") long expiredAfter) {
        this.secret = secret;
        this.expiredAfter = expiredAfter * 1000;
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
    private final String secret;
    private final long expiredAfter;

    public String createToken(String subject, Map<String, Object> claims) {
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiredAfter))
                .withPayload(claims)
                .sign(Algorithm.HMAC256(secret));
    }

    public Map<String, String> convertTokenToMap(String jwtToken) {
        Map<String, String> map = new HashMap<>();
        map.put("header", HEADER);
        map.put("token", TOKEN_PREFIX + jwtToken);

        return map;
    }

    // TODO - verify
    public boolean verifyToken(String jwtToken) {
        boolean result = false;
        if (jwtToken == null || jwtToken.length() == 0) {
            return result;
        }

        long now = System.currentTimeMillis();

        return result;
    }
}
