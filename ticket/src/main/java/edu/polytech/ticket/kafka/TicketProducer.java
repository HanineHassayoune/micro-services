package edu.polytech.ticket.kafka;


import edu.polytech.ticket.entity.TicketEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;

@Service
@EnableKafka
public class TicketProducer {

    private final KafkaTemplate<String, TicketEntity> kafkaTemplate;

    public TicketProducer(KafkaTemplate<String, TicketEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTicket(TicketEntity ticket) {
        // Envoi du message (ticket) sur le topic Kafka
        kafkaTemplate.send("ticket-topic", ticket);
    }
}
