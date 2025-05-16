package edu.polytech.ticket.service;

import edu.polytech.ticket.dto.SolutionDto;
import edu.polytech.ticket.entity.SolutionEntity;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.repository.SolutionRepository;
import edu.polytech.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final SolutionRepository solutionRepository;

    public void saveSolution(SolutionEntity solution, Integer userId) {
        TicketEntity ticket = solution.getTicket();

        if (!userId.equals(ticket.getAssignedUserId())) {
            throw new IllegalArgumentException("You are not assigned to this ticket.");
        }

        solutionRepository.save(solution);
    }


    public List<SolutionEntity> findAllSolutions() {
        return solutionRepository.findAll();
    }


    public SolutionDto mapToDto(SolutionEntity solution) {
        return SolutionDto.builder()
                .id(solution.getId())
                .description(solution.getDescription())
                .reference(solution.getReference())
                .code(solution.getCode())
                .ticketId(solution.getTicket().getId())
                .userId(solution.getCreatedByUserId())
                .datePosted(solution.getDatePosted())  // Mapping datePosted
                .build();
    }



    public SolutionDto findSolutionByTicketId(Integer ticketId) {
        return solutionRepository.findByTicketId(ticketId)
                .map(this::mapToDto)
                .orElse(null);
    }





}
