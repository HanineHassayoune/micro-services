package edu.polytech.notification.repository;

import edu.polytech.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByReceiverId(Integer receiverId);
}

