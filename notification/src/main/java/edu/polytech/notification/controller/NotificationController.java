package edu.polytech.notification.controller;

import edu.polytech.notification.entity.Notification;

import edu.polytech.notification.feign.AuthFeignClientService;
import edu.polytech.notification.service.NotificationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    private final AuthFeignClientService authFeignClientService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Notification notification){
        service.saveNotification(notification);
    }


    @GetMapping
    public ResponseEntity<List<Notification>> findAllNotifications(){
        return ResponseEntity.ok(service.findAllNotifications());
    }

    @GetMapping("/me")
    public ResponseEntity<List<Notification>> findNotificationsForConnectedUser(@RequestHeader("Authorization") String token) {
        Integer userId = authFeignClientService.extractUserIdFromToken(token);
        List<Notification> notifications = service.findNotificationsByReceiver(userId);
        return ResponseEntity.ok(notifications);
    }


}
