package edu.polytech.notification.service;

import edu.polytech.notification.entity.Notification;
import edu.polytech.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    public void saveNotification(Notification notification){
        log.info("💾 Notification enregistrée : {}", notification.getTitle());
        repository.save(notification);
    }

    public List<Notification>findAllNotifications(){
        return repository.findAll();
    }
    public List<Notification> findNotificationsByReceiver(Integer receiverId) {
        return repository.findByReceiverId(receiverId);
    }



}


