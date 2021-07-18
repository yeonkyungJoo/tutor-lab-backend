package com.tutor.tutorlab.config.security.oauth.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
// https://developers.google.com/identity/protocols/oauth2/web-server#httprest_1
public class GoogleOAuth implements OAuth {

    private final String GOOGLE_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String GOOGLE_CALLBACK_URL = "http://localhost:8080/oauth/google/callback";
    private final String GOOGLE_USERINFO_ACCESS_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final String GOOLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_CLIENT_ID = "902783645965-ald60d1ehnaeaoetihtb1861u98ppf3u.apps.googleusercontent.com";
    private final String GOOGLE_CLIENT_SECRET = "U7889QYKM2Zgt-Ui2eEKqKzL";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // 승인 매개변수 설정
    @Override
    public Map<String, String> getOAuthRedirectURL() {

        Map<String, String> map = new HashMap<>();
        map.put("client_id", GOOGLE_CLIENT_ID);
        map.put("redirect_uri", GOOGLE_CALLBACK_URL);
        map.put("response_type", "code");
        map.put("scope", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
        map.put("base_url", GOOGLE_BASE_URL);
        return map;
    }

    // Exchange authorization code for refresh and access tokens
    @Override
    public String requestAccessToken(String code) {

        if (!StringUtils.hasLength(code)) {
            return null;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", GOOGLE_CALLBACK_URL);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOLE_TOKEN_URL, params, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }

        return null;
    }

    @Override
    public String requestUserInfo(String accessToken) {

        if (!StringUtils.hasLength(accessToken)) {
            return null;
        }

        Map<String, String> map = convertStringToMap(accessToken);
        // System.out.println(map);
        String token = map.get("access_token");
        if (!StringUtils.hasLength(token)) {
            return null;
        }

        HttpHeaders header = new HttpHeaders();
        header.set(HEADER, TOKEN_PREFIX + token);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(GOOGLE_USERINFO_ACCESS_URL, HttpMethod.GET, new HttpEntity(header), String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return null;
    }

    @Override
    public Map<String, String> requestLogin(String code) {

        String accessToken = requestAccessToken(code);
        String userInfo = requestUserInfo(accessToken);

        return convertStringToMap(userInfo);
    }

    private Map<String, String> convertStringToMap(String string) {

        try {
            if (StringUtils.hasLength(string)) {
                return objectMapper.readValue(string, Map.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}
