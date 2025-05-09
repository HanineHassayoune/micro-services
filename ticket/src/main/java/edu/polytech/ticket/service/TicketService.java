package edu.polytech.ticket.service;

import edu.polytech.ticket.dto.LogTicketDto;
import edu.polytech.ticket.dto.ProjectDto;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.feign.AuthFeignClient;
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
    private final AuthFeignClient authFeignClient;


/*public void saveTicket(TicketEntity ticket) {
        repository.save(ticket);
        ticketProducer.sendTicket(ticket);
        System.out.println("Ticket enregistré et envoyé à Kafka : " + ticket.getTitle());
    }*/

    public void saveTicket(TicketEntity ticket) {
        try {
            ProjectDto project = authFeignClient.getProjectByTitle(ticket.getProjectName());
            if (project != null && project.getId() != null) {
                ticket.setProjectId(project.getId()); // Lier par ID
                repository.save(ticket);
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


    public LogTicketDto toDto(TicketEntity entity) {
        return LogTicketDto.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .title(entity.getTitle())
                .priority(entity.getPriority())
                .imageUrl(entity.getImageUrl())
                .level(entity.getLevel())
                .date(entity.getDate())
                .projectName(entity.getProjectName())
                .loggerName(entity.getLoggerName())
                .type(entity.getType())
                .projectId(entity.getProjectId())
                .stackTrace(entity.getStackTrace())
                .assignedUserId(entity.getAssignedUserId())
                .build();
    }



}
