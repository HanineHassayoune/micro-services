package edu.polytech.ticket.kafka;


import edu.polytech.ticket.dto.TicketDto;
import edu.polytech.ticket.entity.TicketEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;

/*@Service
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
}*/
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketProducer {

    private final KafkaTemplate<String, TicketDto> kafkaTemplate;

    public void sendTicket(TicketEntity ticket) {
        TicketDto ticketDto = TicketDto.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .projectName(ticket.getProjectName())
                .priority(ticket.getPriority())
                .date(ticket.getDate())
                .category(ticket.getCategory())
                .assignedUserId(ticket.getAssignedUserId())
                .build();
        kafkaTemplate.send("ticket-topic", ticketDto);
        log.info("📦 Ticket publié dans Kafka : {}", ticketDto.getTitle());
    }

}
