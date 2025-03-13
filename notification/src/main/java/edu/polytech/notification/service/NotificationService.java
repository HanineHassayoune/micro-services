package edu.polytech.notification.service;

import edu.polytech.notification.entity.Notification;
import edu.polytech.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    public void saveNotification(Notification notification){
        repository.save(notification);
    }

    public List<Notification>findAllNotifications(){
        return repository.findAll();
    }
}
