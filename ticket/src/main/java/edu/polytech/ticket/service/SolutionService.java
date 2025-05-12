package edu.polytech.ticket.service;

import edu.polytech.ticket.entity.SolutionEntity;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.repository.SolutionRepository;
import edu.polytech.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final TicketRepository ticketRepository;

    public void saveSolution(SolutionEntity solution, Integer userId) {
        TicketEntity ticket = ticketRepository.findById(solution.getTicket().getId())
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        // Vérifie que l'utilisateur est bien assigné au ticket
        if (!userId.equals(ticket.getAssignedUserId())) {
            throw new IllegalArgumentException("You are not assigned to this ticket.");
        }

        // Sauvegarde la solution
        solutionRepository.save(solution);
    }

    public List<SolutionEntity> findAllSolutions() {
        return solutionRepository.findAll();
    }
}
