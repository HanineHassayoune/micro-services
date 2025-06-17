package edu.polytech.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.polytech.ticket.dto.TicketDto;
import edu.polytech.ticket.enums.Priority;
import edu.polytech.ticket.enums.Status;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.feign.AuthFeignClientService;
import edu.polytech.ticket.repository.TicketRepository;
import edu.polytech.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
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
    private final TicketRepository ticketRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping(consumes = {"application/json", "application/x-ndjson"})
    public void createFromFluentd(@RequestBody String rawLog) {
        System.out.println("✅ RECEIVED NDJSON Ticket(s):\n" + rawLog);
        try {
            String[] jsonLines = rawLog.split("\\r?\\n");
            for (String line : jsonLines) {
                if (line.trim().isEmpty()) continue;

                Map<String, Object> logMap = mapper.readValue(line, Map.class);

                TicketDto dto = TicketDto.builder()
                        .status(Status.TO_DO)
                        .title((String) logMap.get("message"))
                        .priority(Priority.LOW)
                        .level((String) logMap.get("level"))
                        .date((String) logMap.get("@timestamp"))
                        .projectName((String) logMap.get("projectName"))
                        .loggerName((String) logMap.get("logger_name"))
                        .stackTrace((String) logMap.get("stack_trace"))
                        .category((String) logMap.get("category"))
                        .build();

                TicketEntity ticket = TicketEntity.builder()
                        .status(dto.getStatus())
                        .title(dto.getTitle())
                        .priority(dto.getPriority())
                        .level(dto.getLevel())
                        .date(dto.getDate())
                        .projectName(dto.getProjectName())
                        .loggerName(dto.getLoggerName())
                        .stackTrace(dto.getStackTrace())
                        .category(dto.getCategory())
                        .build();

                ticketService.saveTicket(ticket);
                System.out.println("🎫 Ticket enregistré depuis Fluentd !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible de parser ou enregistrer le(s) ticket(s).");
        }
    }

    @PostMapping("/manual")
    public ResponseEntity<String> createManualTicket(@RequestBody TicketDto dto) {
        try {
            TicketEntity ticket = TicketEntity.builder()
                    .status(dto.getStatus())
                    .title(dto.getTitle())
                    .priority(dto.getPriority())
                    .level(dto.getLevel())
                    .date(dto.getDate())
                    .projectName(dto.getProjectName())
                    .loggerName(dto.getLoggerName())
                    .stackTrace(dto.getStackTrace())
                    .category(dto.getCategory())
                    .assignedUserId(dto.getAssignedUserId())
                    .imageUrl(dto.getImageUrl())
                    .build();

            ticketService.saveTicket(ticket);
            System.out.println("🎫 Ticket created manually !");
            return ResponseEntity.status(HttpStatus.CREATED).body("✅ Ticket created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Failed to create ticket.");
        }
    }


    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TicketDto>> getTicketsByProjectId(@PathVariable Integer projectId) {
        List<TicketDto> dtos = ticketService.getTicketsByProjectSmart(projectId).stream()
                .map(ticketService::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Integer ticketId) {
        TicketEntity ticket = ticketService.findTicketById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found with ID: " + ticketId));
        return ResponseEntity.ok(ticketService.toDto(ticket));
    }

    @PutMapping("/{ticketId}/assign")
    public ResponseEntity<TicketDto> assignUserToTicket(
            @PathVariable Integer ticketId,
            @RequestParam Integer userId
    ) {
        TicketEntity ticket = ticketService.findTicketById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        ticket.setAssignedUserId(userId);
        ticketService.saveTicket(ticket);
        return ResponseEntity.ok(ticketService.toDto(ticket));
    }


   /* @GetMapping("/project/{projectId}/me")
    public ResponseEntity<List<TicketDto>> getMyTicketsByProject(
            @PathVariable Integer projectId,
            @RequestHeader("Authorization") String token
    ) {
        Integer userId = authFeignClientService.extractUserIdFromToken(token);
        Role role = authFeignClientService.extractUserRoleFromToken(token);

        if (!Role.DEVELOPER.equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Access Denied
        }

        List<TicketEntity> tickets = ticketService.getTicketsByProjectIdAndUserId(projectId, userId);

        List<TicketDto> dtos = tickets.stream()
                .map(ticketService::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

*/
   @GetMapping("/project/{projectId}/filter")
   public ResponseEntity<List<TicketDto>> filterTicketsByCategoryAndUser(
           @PathVariable Integer projectId,
           @RequestParam(required = false) String category,
           @RequestParam(required = false) String assignedUserName
   ) {
       List<TicketDto> result = ticketService
               .filterTickets(projectId, category, assignedUserName)
               .stream()
               .map(ticketService::toDto)
               .toList();

       return ResponseEntity.ok(result);
   }

    @PutMapping("/{ticketId}/status")
    public ResponseEntity<TicketDto> updateTicketStatus(
            @PathVariable Integer ticketId,
            @RequestParam("status") Status status
    ) {
        TicketDto updated = ticketService.updateTicketStatus(ticketId, status);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{ticketId}/priority")
    public ResponseEntity<TicketDto> updateTicketPriority(
            @PathVariable Integer ticketId,
            @RequestParam("priority") Priority priority
    ) {
        TicketDto updated = ticketService.updateTicketPriority(ticketId, priority);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/{ticketId}/related")
    public ResponseEntity<List<TicketDto>> getTicketsWithSameCategory(@PathVariable Integer ticketId) {
        TicketEntity currentTicket = ticketService.findTicketById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        String category = currentTicket.getCategory();

        List<TicketDto> relatedTickets = ticketService.findDoneTicketsByCategory(category).stream()
                .filter(ticket -> !ticket.getId().equals(ticketId)) // Exclure le ticket lui-même
                .map(ticketService::toDto)
                .toList();

        return ResponseEntity.ok(relatedTickets);
    }


    // Renvoyer tous les tickets d'un projet
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<TicketDto>> getTicketsByProject(@PathVariable Integer projectId) {
        List<TicketDto> dtos = ticketService.getTicketsByProjectSmart(projectId)
                .stream()
                .map(ticketService::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Renvoyer le nombre de tickets par statut pour un projet
    @GetMapping("/statuses/{projectId}")
    public ResponseEntity<Map<String, Long>> getTicketStatusesByProject(@PathVariable Integer projectId) {
        Map<String, Long> result = ticketService.countTicketsByStatus(projectId);
        return ResponseEntity.ok(result);
    }

    // Renvoyer le nombre de tickets par catégorie pour un projet
    @GetMapping("/categories/percentages/{projectId}")
    public ResponseEntity<Map<String, Double>> getTicketCategoriesPercentageByProject(@PathVariable Integer projectId) {
        Map<String, Double> result = ticketService.getTicketCategoriesPercentage(projectId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/priorities/{projectId}")
    public ResponseEntity<Map<String, Long>> getTicketPrioritiesByProject(@PathVariable Integer projectId) {
        Map<String, Long> result = ticketService.countTicketsByPriority(projectId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/project/{projectId}/category-priority")
    public ResponseEntity<Map<String, Map<String, Long>>> getTicketsByCategoryAndPriority(
            @PathVariable Integer projectId) {
        Map<String, Map<String, Long>> result = ticketService.countTicketsByCategoryAndPriority(projectId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/count-by-category-priority/{projectId}")
    public ResponseEntity<Map<String, Map<String, Long>>> getCountByCategoryAndPriority(
            @PathVariable Integer projectId) {
        Map<String, Map<String, Long>> counts = ticketService.countTicketsByCategoryAndPriority(projectId);
        return ResponseEntity.ok(counts);
    }

}

