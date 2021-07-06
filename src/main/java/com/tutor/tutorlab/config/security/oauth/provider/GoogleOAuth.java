package com.tutor.tutorlab.config.security.oauth.provider;

import java.util.HashMap;
import java.util.Map;

public class GoogleOAuth implements OAuth {

    private final String GOOGLE_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String GOOGLE_CALLBACK_URL = "http://localhost:8080/login/oauth/google/callback";
    private final String GOOGLE_USERINFO_ACCESS_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final String GOOGLE_CLIENT_ID = "";
    private final String GOOGLE_CLIENT_SECRET = "";

    @Override
    public Map<String, String> getOAuthRedirectURL() {

        Map<String, String> map = new HashMap<>();
        map.put("base_url", GOOGLE_BASE_URL);
        map.put("scope", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
        map.put("response_type", "code");



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
}
