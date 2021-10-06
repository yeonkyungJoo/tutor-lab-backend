package com.tutor.tutorlab.modules.notification.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.notification.enums.NotificationType;
import com.tutor.tutorlab.modules.notification.repository.NotificationRepository;
import com.tutor.tutorlab.modules.notification.vo.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.NOTIFICATION;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.USER;

@Transactional
@RequiredArgsConstructor
@Service
public class NotificationService extends AbstractService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Notification> getNotifications(User user, Integer page) {
        return notificationRepository.findByUser(user, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    public Notification createNotification(Long userId, NotificationType type) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER));
        return this.createNotification(user, type);
    }

    public Notification createNotification(User user, NotificationType type) {

        Notification notification = Notification.of(
                user,
                type
        );
        return notificationRepository.save(notification);
    }

    public void check(User user, Long notificationId) {
        Notification notification = notificationRepository.findByUserAndId(user, notificationId)
                .orElseThrow(() -> new EntityNotFoundException(NOTIFICATION));
        notification.check();
    }

    public void deleteNotification(User user, Long notificationId) {

        // TODO - CHECK : 쿼리 비교 - notificationRepository.deleteById(notificationId);
        Notification notification = notificationRepository.findByUserAndId(user, notificationId)
                .orElseThrow(() -> new EntityNotFoundException(NOTIFICATION));
        notificationRepository.delete(notification);
    }

    public void deleteNotifications(User user, List<Long> notificationIds) {
        // TODO - CHECK
        notificationRepository.deleteAllById(notificationIds);
        // notificationRepository.deleteAllByIdInBatch(notificationIds);
    }
}
