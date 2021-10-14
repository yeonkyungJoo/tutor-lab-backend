package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.response.NotificationResponse;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"NotificationController"})
@RequestMapping("/users/my-notifications")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation("알림 리스트 - 페이징")
    @GetMapping
    public ResponseEntity<?> getNotifications(@CurrentUser User user,
                                              @RequestParam(defaultValue = "1") Integer page) {

        Page<NotificationResponse> notifications = notificationService.getNotificationResponses(user, page);
        return ResponseEntity.ok(notifications);
    }

    @ApiOperation("알림 확인")
    @PutMapping("/{notification_id}")
    public ResponseEntity<?> getNotification(@CurrentUser User user,
                                             @PathVariable(name = "notification_id") Long notificationId) {
        notificationService.check(user, notificationId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("알림 삭제")
    @DeleteMapping("/{notification_id}")
    public ResponseEntity<?> deleteNotification(@CurrentUser User user,
                                                @PathVariable(name = "notification_id") Long notificationId) {
        notificationService.deleteNotification(user, notificationId);
        return ResponseEntity.ok().build();
    }

    // TODO - 알림 전체 삭제 / 선택 삭제
    @ApiOperation("알림 선택 삭제")
    @DeleteMapping
    public ResponseEntity<?> deleteNotifications(@CurrentUser User user,
                                                 @RequestParam(value = "notification_ids") List<Long> notificationIds) {
        notificationService.deleteNotifications(user, notificationIds);
        return ResponseEntity.ok().build();
    }
}
