package com.tutor.tutorlab.config.security.oauth.provider;

import java.util.Map;

public class KakaoOAuth implements OAuth {

    @Override
    public Map<String, String> getOAuthRedirectURL() {
        return null;
    }

    @Override
    public String requestAccessToken(String code) {
        return null;
    }

    @Override
    public String requestUserInfo(String accessToken) {
        return null;
    }

    @Override
    public Map<String, String> requestLogin(String code) {
        return null;
    }
}
