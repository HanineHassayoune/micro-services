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

@RestController
@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService service;
    private final TicketRepository ticketRepository;

    @PostMapping
    public ResponseEntity<String> saveSolution(@RequestBody SolutionDto dto) {
        // Recherche du ticket
        TicketEntity ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        // Création de l'entité Solution
        SolutionEntity solution = SolutionEntity.builder()
                .description(dto.getDescription())
                .reference(dto.getReference())
                .code(dto.getCode())
                .ticket(ticket)
                .createdByUserId(dto.getUserId()) // set here directly
                .build();

        // Appel au service avec vérification d'assignation
        service.saveSolution(solution, dto.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body("Solution saved successfully.");
    }


    @GetMapping
    public ResponseEntity<List<SolutionEntity>> findAllSolutions() {
        return ResponseEntity.ok(service.findAllSolutions());
    }
}
