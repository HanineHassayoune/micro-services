package edu.polytech.ticket.service;

import edu.polytech.ticket.dto.TicketDto;
import edu.polytech.ticket.dto.ProjectDto;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.enums.Status;
import edu.polytech.ticket.feign.AuthFeignClientService;
import edu.polytech.ticket.kafka.TicketProducer;
import edu.polytech.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repository;
    private final TicketProducer ticketProducer;
    private final AuthFeignClientService authFeignClientService;


/*public void saveTicket(TicketEntity ticket) {
        repository.save(ticket);
        ticketProducer.sendTicket(ticket);
        System.out.println("Ticket enregistré et envoyé à Kafka : " + ticket.getTitle());
    }*/

    public void saveTicket(TicketEntity ticket) {
        try {
            ProjectDto project = authFeignClientService.getProjectByTitle(ticket.getProjectName());
            if (project != null && project.getId() != null) {
                ticket.setProjectId(project.getId()); // Lier par ID
                repository.save(ticket);
                System.out.println("📤 Envoi du ticket à Kafka...");
                ticketProducer.sendTicket(ticket);
            } else {
                System.err.println("⚠️ Projet trouvé mais ID nul : " + ticket.getProjectName());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Projet introuvable ou erreur lors de la récupération : " + ticket.getProjectName());
            e.printStackTrace();
        }
    }


    public List<TicketEntity> findAllTickets() {
        return repository.findAll();
    }


    public List<TicketEntity> getTicketsByProjectId(Long projectId) {
        return repository.findByProjectId(projectId);
    }



    public Optional<TicketEntity> findTicketById(Integer id) {
        return repository.findById(id);
    }


    public TicketDto toDto(TicketEntity entity) {
        return TicketDto.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .title(entity.getTitle())
                .priority(entity.getPriority())
                .imageUrl(entity.getImageUrl())
                .level(entity.getLevel())
                .date(entity.getDate())
                .projectName(entity.getProjectName())
                .loggerName(entity.getLoggerName())
                .category(entity.getCategory())
                .projectId(entity.getProjectId())
                .stackTrace(entity.getStackTrace())
                .assignedUserId(entity.getAssignedUserId())
                .build();
    }


    public List<TicketEntity> getTicketsByProjectIdAndUserId(Long projectId, Integer userId) {
        return repository.findByProjectIdAndAssignedUserId(projectId, userId);
    }


    public TicketDto updateTicketStatus(Integer ticketId, Status newStatus) {
        TicketEntity ticket = repository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));

        ticket.setStatus(newStatus);
        TicketEntity saved = repository.save(ticket);

        // Optionnel : renvoyer le ticket modifié sous forme de DTO
        return toDto(saved);
    }




}
