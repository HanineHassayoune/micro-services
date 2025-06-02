package edu.polytech.ticket.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.polytech.ticket.dto.TicketDto;
import edu.polytech.ticket.entity.TicketEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

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
        try {
            String json = objectMapper.writeValueAsString(ticketDto);
            kafkaTemplate.send("ticket-topic", json);
            log.info("📦 Ticket JSON publié dans Kafka : {}", json);
        } catch (Exception e) {
            log.error("❌ Erreur de sérialisation du ticket : {}", e.getMessage());
        }
    }
}
