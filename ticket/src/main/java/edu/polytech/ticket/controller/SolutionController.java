package edu.polytech.ticket.controller;

import edu.polytech.ticket.dto.SolutionDto;
import edu.polytech.ticket.entity.SolutionEntity;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.repository.TicketRepository;
import edu.polytech.ticket.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService service;
    private final TicketRepository ticketRepository;

    @PostMapping
    public ResponseEntity<Map<String, String>> saveSolution(@RequestBody SolutionDto dto) {
        // Recherche du ticket
        TicketEntity ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        // Création de l'entité Solution
        SolutionEntity solution = SolutionEntity.builder()
                .description(dto.getDescription())
                .reference(dto.getReference())
                .code(dto.getCode())
                .ticket(ticket)
                .createdByUserId(dto.getUserId())
                .build();

        // Appel au service avec vérification d'assignation
        service.saveSolution(solution, dto.getUserId());

        // ✅ Réponse JSON : { "message": "Solution saved successfully." }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Solution saved successfully."));
    }


    @GetMapping
    public ResponseEntity<List<SolutionEntity>> findAllSolutions() {
        return ResponseEntity.ok(service.findAllSolutions());
    }


    @GetMapping("/by-ticket/{ticketId}")
    public ResponseEntity<?> getSolutionByTicketId(@PathVariable Integer ticketId) {
        SolutionDto solutionDto = service.findSolutionByTicketId(ticketId);
        if (solutionDto == null) {
            // Retourner 200 avec un message JSON
            return ResponseEntity.ok().body(
                    Map.of("message", "Aucune solution trouvée pour le ticket ID: " + ticketId)
            );
        }
        return ResponseEntity.ok(solutionDto);
    }







}
