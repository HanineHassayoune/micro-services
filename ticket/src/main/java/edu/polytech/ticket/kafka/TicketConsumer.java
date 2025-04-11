package edu.polytech.ticket.kafka;

import edu.polytech.ticket.entity.TicketEntity;
import org.springframework.kafka.annotation.KafkaListener;

public class TicketConsumer {
    @KafkaListener(topics = "ticket-topic", groupId = "notification-group")
    public void consumeTicket(TicketEntity ticket) {
        // Logique pour traiter le ticket reçu
        System.out.println("Ticket reçu : " + ticket);
        // Appel de la logique de notification
        sendNotification(ticket);
    }

    private void sendNotification(TicketEntity ticket) {
        // Logique pour envoyer une notification en fonction du ticket
        System.out.println("Notification envoyée pour le ticket : " + ticket.getTitle());
    }
}
