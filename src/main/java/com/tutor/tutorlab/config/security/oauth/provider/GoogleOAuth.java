package com.tutor.tutorlab.config.security.oauth.provider;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class GoogleOAuth implements OAuth {

    private final String GOOGLE_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String GOOGLE_CALLBACK_URL = "http://localhost:8080/login/oauth/google/callback";
    private final String GOOGLE_USERINFO_ACCESS_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final String GOOLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_CLIENT_ID = "";
    private final String GOOGLE_CLIENT_SECRET = "";

    @Override
    public Map<String, String> getOAuthRedirectURL() {

        Map<String, String> map = new HashMap<>();
        map.put("base_url", GOOGLE_BASE_URL);
        map.put("redirect_uri", GOOGLE_CALLBACK_URL);
        map.put("scope", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
        map.put("response_type", "code");
        map.put("client_id", GOOGLE_CLIENT_ID);

        return map;
    }

    @Override
    public String requestAccessToken(String code) {

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = (new RestTemplate()).postForEntity(GOOLE_TOKEN_URL, params, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }

        return null;
    }

    @Override
    public String requestUserInfo(String accessToken) {

        return null;
    }
}
