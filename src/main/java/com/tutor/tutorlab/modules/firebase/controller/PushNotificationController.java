package com.tutor.tutorlab.modules.firebase.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.tutor.tutorlab.modules.account.service.UserService;
import com.tutor.tutorlab.modules.firebase.service.AndroidPushNotificationsService;
import com.tutor.tutorlab.modules.firebase.service.AndroidPushPeriodicNotifications;
import io.swagger.annotations.Api;
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
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"PushNotificationController"})
@RequiredArgsConstructor
@RestController
public class PushNotificationController {

    private final AndroidPushNotificationsService androidPushNotificationsService;
    private final UserService userService;

    @ApiIgnore
    @GetMapping(value = "/send")
    public ResponseEntity<?> send() throws ExecutionException, InterruptedException {

        CompletableFuture<String> pushNotification
                = androidPushNotificationsService.send("fcmToken", "title", "content");
        CompletableFuture.allOf(pushNotification).join();

        return ResponseEntity.ok(pushNotification.get());
    }

    // 콜백
    @ApiIgnore
    @GetMapping("/set-fcmToken")
    public ResponseEntity<?> setFcmToken(@RequestParam(name = "username") String username,
                                         @RequestParam(name = "fcmToken") String fcmToken) {
        // System.out.println("fcmToken = " + fcmToken);
        userService.updateUserFcmToken(username, fcmToken);
        return ResponseEntity.ok().build();
    }
}
