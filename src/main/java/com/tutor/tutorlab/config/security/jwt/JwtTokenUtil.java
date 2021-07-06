package com.tutor.tutorlab.config.security.jwt;

import lombok.Data;

@Data
public class JwtTokenUtil {

    public static final long MINUTE = (1000L * 60);
    public static final long HOUR = (1000L * 60 * 60);
    public static final long DAY = (1000L * 60 * 60 * 24);
    public static final long MONTY = (1000L * 60 * 60 * 24 * 30);
    public static final long YEAR = (1000L * 60 * 60 * 24 * 365);

    public static final long DEFAULT_EXPIRED_AT = MINUTE * 10;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

    private String secret;
    private long expiredAt = DEFAULT_EXPIRED_AT;


}
