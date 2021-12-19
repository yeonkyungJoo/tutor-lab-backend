package com.tutor.tutorlab.modules.firebase.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// firebase_server_key = firebase project > cloud messaging > server key
@Service
public class AndroidPushNotificationsService {

    @Value("${firebase.server.key}")
    private String firebase_server_key;
    @Value("${firebase.api.url}")
    private String firebase_api_url;

    private HttpEntity<String> getRequest(String fcmToken, String title, String content) {

        String notifications = AndroidPushPeriodicNotifications.PeriodicNotificationJson(fcmToken, title, content);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new HttpEntity<>(notifications, headers);
    }

    @Async
    public CompletableFuture<String> send(String fcmToken, String title, String content) {

        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor(HttpHeaders.AUTHORIZATION,  "key=" + firebase_server_key));
        interceptors.add(new HeaderRequestInterceptor(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url, getRequest(fcmToken, title, content), String.class);
        return CompletableFuture.completedFuture(firebaseResponse);
    }
}