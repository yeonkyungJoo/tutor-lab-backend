package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.notification.vo.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@Transactional
@MockMvcTest
class NotificationControllerIntegrationTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;

    private Lecture lecture;
    private User tuteeUser;

    @BeforeEach
    void init() {

        // 튜티
        SignUpRequest signUpRequest = getSignUpRequest("tutee", "tutee");
        tuteeUser = loginService.signUp(signUpRequest);
        loginService.verifyEmail(tuteeUser.getUsername(), tuteeUser.getEmailVerifyToken());
    }

    @DisplayName("튜티가 강의 수강 시 튜터에게 알림이 오는지 확인")
    @WithAccount(NAME)
    @Test
    void getNotifications_enrollment() throws Exception {

        // Given
        // 튜터
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);
        lecture = lectureService.createLecture(user, lectureCreateRequest);

        // When
        enrollmentService.createEnrollment(tuteeUser, lecture.getId(), lecture.getLecturePrices().get(0).getId());

        // Then
        mockMvc.perform(get("/users/my-notifications"))
                .andDo(print())
                .andExpect(status().isOk());
        List<Notification> notifications = notificationRepository.findByUser(user);
        notifications.stream()
                .forEach(notification -> assertFalse(notification.isChecked()));
    }

    // TODO - TEST
//    @DisplayName("채팅 메세지 도착 시 알림 오는지 확인")
//    @WithAccount(NAME)
//    @Test
//    void getNotifications_chat() {
//
//        // Given
//        // When
//        // Then
//    }

    @DisplayName("알림 확인")
    @WithAccount(NAME)
    @Test
    void getNotification() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);
        lecture = lectureService.createLecture(user, lectureCreateRequest);

        enrollmentService.createEnrollment(tuteeUser, lecture.getId(), lecture.getLecturePrices().get(0).getId());
        List<Notification> notifications = notificationRepository.findByUser(user);
        Notification notification = notifications.get(0);
        Long notificationId = notification.getId();

        // When
        mockMvc.perform(put("/users/my-notifications/{notification_id}", notificationId))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        notification = notificationRepository.findById(notificationId).orElse(null);
        assertNotNull(notification);
        assertTrue(notification.isChecked());
    }

    @WithAccount(NAME)
    @Test
    void deleteNotification() throws Exception {

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
        mockMvc.perform(delete("/users/my-notifications/{notification_id}", notificationId))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        assertTrue(notificationRepository.findById(notificationId).isEmpty());
    }

    @WithAccount(NAME)
    @Test
    void deleteNotifications() throws Exception {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);
        lecture = lectureService.createLecture(user, lectureCreateRequest);

        enrollmentService.createEnrollment(tuteeUser, lecture.getId(), lecture.getLecturePrices().get(0).getId());
        List<Notification> notifications = notificationRepository.findByUser(user);
        assertEquals(1, notifications.size());
        Notification notification = notifications.get(0);
        System.out.println(notification);

        // When
        mockMvc.perform(delete("/users/my-notifications")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("notification_ids", String.valueOf(notification.getId())))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        notifications = notificationRepository.findAllById(Arrays.asList(notification.getId()));
        assertEquals(0, notifications.size());
    }
}