package edu.polytech.ticket.controller;

import edu.polytech.ticket.dto.SolutionDto;
import edu.polytech.ticket.entity.SolutionEntity;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.enums.Status;
import edu.polytech.ticket.gitHub.GitHubService;
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
    private final GitHubService gitHubService;

    @PostMapping
    public ResponseEntity<Map<String, String>> saveSolution(@RequestBody SolutionDto dto) {
        // Recherche du ticket
        TicketEntity ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        // Vérifie que le ticket est en IN_PROGRESS ==> add dolution to ticket only when status is IN_PROGRESS
        if (ticket.getStatus() != Status.IN_PROGRESS) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "You can only add a solution when the ticket is in IN_PROGRESS status."));
        }

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

        // Push le code dans la branche GitHub
        String branch = ticket.getBranchName();
        String fileName = "solution_ticket_" + ticket.getId() + ".java";
        String commitMessage = "Add solution to ticket " + ticket.getId();

        try {
            gitHubService.pushFileToBranch(
                    ticket.getProjectName(),
                    branch,
                    fileName,
                    solution.getCode(),
                    commitMessage
            );
        } catch (Exception e) {
            System.err.println("Échec du push de solution sur GitHub : " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Solution saved and pushed successfully."));
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
