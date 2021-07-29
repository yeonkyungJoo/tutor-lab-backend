package com.tutor.tutorlab.config.security.oauth.provider.naver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.security.oauth.provider.OAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
// https://developers.naver.com/docs/login/api/api.md
public class NaverOAuth implements OAuth {

    private final String NAVER_BASE_URL = "https://nid.naver.com/oauth2.0/authorize";
    private final String NAVER_CALLBACK_URL = "http://localhost:8080/oauth/naver/callback";
    private final String NAVER_USERINFO_ACCESS_URL = "https://openapi.naver.com/v1/nid/me";
    private final String NAVER_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
    private final String NAVER_CLIENT_ID = "NNG0ZvRBJlxlE5DbApJR";
    private final String NAVER_CLIENT_SECRET = "V23oWh9UTy";

    private final HttpSession session;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String requestAccessToken(String code) {

        if (!StringUtils.hasLength(code)) {
            return null;
        }

        /*
        * Required Parameter
        - grant_type
        - client_id
        - client_secret
        - code
        - state
        */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", NAVER_CALLBACK_URL);
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("code", code);
        params.add("state", (String) session.getAttribute("state"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(NAVER_TOKEN_URL, HttpMethod.POST, request, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println(responseEntity.getBody());
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
        String token = map.get("access_token");
        if (!StringUtils.hasLength(token)) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER, TOKEN_PREFIX + token);
        ResponseEntity<String> responseEntity
                = restTemplate.exchange(NAVER_USERINFO_ACCESS_URL, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }

        return null;

    }

    // TODO - 리팩토링
    @Override
    public String requestLogin(String code) {

        String accessToken = requestAccessToken(code);
        String userInfo = requestUserInfo(accessToken);

        return userInfo;
    }

    public NaverResponse getUserInfo(String code) {

        try {
            NaverResponse naverResponse = objectMapper.readValue(requestLogin(code), NaverResponse.class);
            return naverResponse;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    // TODO - 리팩토링
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
