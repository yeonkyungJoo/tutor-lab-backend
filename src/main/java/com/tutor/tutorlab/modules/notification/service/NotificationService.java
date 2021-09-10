package com.tutor.tutorlab.modules.notification.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.notification.repository.NotificationRepository;
import com.tutor.tutorlab.modules.notification.vo.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = false)
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification check(User user, Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 데이터입니다."));
        notification.check();
        return notification;
    }

    public void deleteNotification(Long notificationId) {

        // TODO - CHECK : 쿼리 비교
        // notificationRepository.deleteById(notificationId);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 데이터입니다."));
        notificationRepository.delete(notification);
    }

    public void deleteNotifications(List<Long> notificationIds) {
        // TODO - CHECK
        notificationRepository.deleteAllById(notificationIds);
        // notificationRepository.deleteAllByIdInBatch(notificationIds);
    }
}
