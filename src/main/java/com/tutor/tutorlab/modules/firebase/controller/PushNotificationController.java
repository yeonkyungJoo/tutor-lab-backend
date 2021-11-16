package com.tutor.tutorlab.modules.firebase.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.tutor.tutorlab.modules.firebase.service.AndroidPushNotificationsService;
import com.tutor.tutorlab.modules.firebase.service.AndroidPushPeriodicNotifications;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PushNotificationController {

    private final AndroidPushNotificationsService androidPushNotificationsService;

    @GetMapping(value = "/send")
    public ResponseEntity<?> send() throws ExecutionException, InterruptedException {

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send();
        CompletableFuture.allOf(pushNotification).join();

        return ResponseEntity.ok(pushNotification.get());
    }

    @GetMapping("/test-token")
    public void testToken(@RequestParam(name = "fcmToken", required = false) String fcmToken) {
        System.out.println("fcmToken=="+fcmToken);
    }
}
