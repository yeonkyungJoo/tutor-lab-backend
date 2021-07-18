package com.tutor.tutorlab.config.security.oauth.provider;

import java.util.Map;

public interface OAuth {

    public static final String HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    Map<String, String> getOAuthRedirectURL();

    String requestAccessToken(String code);

    String requestUserInfo(String accessToken);

    Map<String, String> requestLogin(String code);

}
