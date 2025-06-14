package edu.polytech.notification.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.polytech.notification.dto.TicketDto;
import edu.polytech.notification.entity.Notification;
import edu.polytech.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = "ticket-topic",
            groupId = "notification-group"
    )
    public void consumeTicket(String data) {
        log.info("🎧 Kafka listener activé !");
        log.info("📨 Message reçu : {}", data);

        try {
            // Convertir le JSON reçu en objet TicketDto
            TicketDto ticket = objectMapper.readValue(data, TicketDto.class);
            log.info("🎯 Nouveau ticket reçu via Kafka : {}", ticket.getTitle());

            // Construire la notification
            Notification notification = Notification.builder()
                    .title("Nouveau ticket : " + ticket.getTitle())
                    .message("Un ticket a été créé dans le projet " + ticket.getProjectName())
                    .ticketId(ticket.getId())
                    .receiverId(ticket.getManagerId())  // ✅ utiliser directement le managerId transmis
                    .build();

            // 1. Sauvegarde en base
            notificationService.saveNotification(notification);

            // 2. Envoi via WebSocket au client ciblé
            messagingTemplate.convertAndSend("/topic/notifications", notification);

        } catch (Exception e) {
            log.error("🚨 Erreur lors de la désérialisation ou traitement du ticket : {}", e.getMessage(), e);
        }
    }
}
