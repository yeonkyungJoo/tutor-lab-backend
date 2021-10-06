package com.tutor.tutorlab.modules.notification.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;

    @DisplayName("튜티가 강의 수강 시 튜터에게 알림이 오는지 확인")
    @Test
    void getNotifications() {
    }

    @Test
    void check() {
        // Given
        // When
        // Then
    }

    @Test
    void deleteNotification() {
        // Given
        // When
        // Then
    }

    @Test
    void deleteNotifications() {
        // Given
        // When
        // Then
    }
}