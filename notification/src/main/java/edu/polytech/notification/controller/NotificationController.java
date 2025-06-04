package edu.polytech.notification.controller;

import edu.polytech.notification.entity.Notification;

import edu.polytech.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Notification notification){
        service.saveNotification(notification);
    }


    @GetMapping
    public ResponseEntity<List<Notification>> findAllNotifications(){
        return ResponseEntity.ok(service.findAllNotifications());
    }





}
