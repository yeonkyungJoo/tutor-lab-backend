package com.tutor.tutorlab.modules.notification.service;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.notification.enums.NotificationType;
import com.tutor.tutorlab.modules.notification.vo.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class NotificationServiceIntegrationTest extends AbstractTest {

    private Lecture lecture;
    private User tuteeUser;

    @BeforeEach
    void init() {

        // 튜티
        SignUpRequest signUpRequest = getSignUpRequest("tutee", "tutee");
        tuteeUser = loginService.signUp(signUpRequest);
        loginService.verifyEmail(tuteeUser.getUsername(), tuteeUser.getEmailVerifyToken());
    }

    @WithAccount(NAME)
    @DisplayName("튜티가 강의 수강 시 튜터에게 알림이 오는지 확인")
    @Test
    void getNotifications() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);
        lecture = lectureService.createLecture(user, lectureCreateRequest);

        // When
        enrollmentService.createEnrollment(tuteeUser, lecture.getId(), lecture.getLecturePrices().get(0).getId());

        // Then
        List<Notification> notifications = notificationRepository.findByUser(user);
        notifications.stream()
                .forEach(notification -> {
                        assertFalse(notification.isChecked());
                        assertEquals(NotificationType.ENROLLMENT, notification.getType());
                });
    }

    @WithAccount(NAME)
    @Test
    void check() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);
        lecture = lectureService.createLecture(user, lectureCreateRequest);

        enrollmentService.createEnrollment(tuteeUser, lecture.getId(), lecture.getLecturePrices().get(0).getId());
        List<Notification> notifications = notificationRepository.findByUser(user);
        Notification notification = notifications.get(0);
        Long notificationId = notification.getId();

        // When
        notificationService.check(user, notificationId);

        // Then
        notification = notificationRepository.findById(notificationId).orElse(null);
        assertNotNull(notification);
        assertTrue(notification.isChecked());
    }

    @WithAccount(NAME)
    @Test
    void deleteNotification() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);
        lecture = lectureService.createLecture(user, lectureCreateRequest);

        enrollmentService.createEnrollment(tuteeUser, lecture.getId(), lecture.getLecturePrices().get(0).getId());
        List<Notification> notifications = notificationRepository.findByUser(user);
        assertEquals(1, notifications.size());
        Notification notification = notifications.get(0);
        Long notificationId = notification.getId();

        // When
        notificationService.deleteNotification(user, notificationId);

        // Then
        assertTrue(notificationRepository.findById(notificationId).isEmpty());
    }

    @WithAccount(NAME)
    @Test
    void deleteNotifications() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);
        lecture = lectureService.createLecture(user, lectureCreateRequest);

        enrollmentService.createEnrollment(tuteeUser, lecture.getId(), lecture.getLecturePrices().get(0).getId());
        List<Notification> notifications = notificationRepository.findByUser(user);
        assertEquals(1, notifications.size());
        Notification notification = notifications.get(0);

        // When
        List<Long> notificationIds = Arrays.asList(notification.getId());
        notificationService.deleteNotifications(user, notificationIds);

        // Then
        notifications = notificationRepository.findAllById(notificationIds);
        assertEquals(0, notifications.size());
    }
}