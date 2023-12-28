package com.example.notification_services.dao.api;

import com.example.notification_services.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification , Long> {
    @Override
    Notification save(Notification notification);
    List<Notification> findAllByUserId(long userId);
    Notification findByNotificationId(long notificationId);
}
