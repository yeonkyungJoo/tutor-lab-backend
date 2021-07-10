package com.tutor.tutorlab.config.security.oauth.provider;

import java.util.Map;

public interface OAuth {

    Map<String, String> getOAuthRedirectURL();

    String requestAccessToken(String code);

    String requestUserInfo(String accessToken);

}
