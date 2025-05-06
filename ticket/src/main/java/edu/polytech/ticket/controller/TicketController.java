package edu.polytech.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.polytech.ticket.dto.LogTicketDto;
import edu.polytech.ticket.entity.Priority;
import edu.polytech.ticket.entity.Status;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.feign.AuthFeignClientService;
import edu.polytech.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final AuthFeignClientService authFeignClientService;
    private final TicketService ticketService;

    @PostMapping(consumes = {"application/json", "application/x-ndjson"})
    public void createFromFluentd(@RequestBody String rawLog) {
        System.out.println("✅ RECEIVED NDJSON Ticket(s):\n" + rawLog);
        ObjectMapper mapper = new ObjectMapper();

        try {
            String[] jsonLines = rawLog.split("\\r?\\n"); // Split NDJSON into lines
            for (String line : jsonLines) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                Map<String, Object> logMap = mapper.readValue(line, Map.class);

                LogTicketDto dto = LogTicketDto.builder()
                        .status(Status.PENDING)
                        .title((String) logMap.get("message"))
                        .priority(Priority.LOW)
                        //.description((String) logMap.get("message"))
                        .level((String) logMap.get("level"))
                        .date((String) logMap.get("@timestamp"))
                        .projectName((String) logMap.get("projectName"))
                        .loggerName((String) logMap.get("logger_name"))
                        .stackTrace((String) logMap.get("stack_trace"))
                        .type((String) logMap.get("exception"))
                        .build();

                TicketEntity ticket = TicketEntity.builder()
                        .status(dto.getStatus())
                        .title(dto.getTitle())
                        //.description(dto.getDescription())
                        .date(dto.getDate())
                        .level(dto.getLevel())
                        .loggerName(dto.getLoggerName())
                        .projectName(dto.getProjectName())
                        .priority(dto.getPriority())
                        .stackTrace(dto.getStackTrace())
                        .type(dto.getType())
                        .build();

                ticketService.saveTicket(ticket);
                System.out.println("🎫 Ticket enregistré depuis Fluentd !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible de parser ou enregistrer le(s) ticket(s).");
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TicketEntity>> getTicketsByProjectId(@PathVariable Long projectId) {
        List<TicketEntity> tickets = ticketService.getTicketsByProjectId(projectId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketEntity> getTicketById(@PathVariable Integer ticketId) {
        return ticketService.findTicketById(ticketId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found with ID: " + ticketId));
    }


}
