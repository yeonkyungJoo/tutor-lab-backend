package com.tutor.tutorlab.modules.notification.repository;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.notification.vo.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByUserAndId(User user, Long notificationId);
    List<Notification> findByUser(User user);
    Page<Notification> findByUser(User user, Pageable pageable);
}
