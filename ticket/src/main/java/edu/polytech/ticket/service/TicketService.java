package edu.polytech.ticket.service;

import edu.polytech.ticket.dto.TicketDto;
import edu.polytech.ticket.dto.ProjectDto;
import edu.polytech.ticket.dto.UserDto;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.enums.Architecture;
import edu.polytech.ticket.enums.Priority;
import edu.polytech.ticket.enums.Status;
import edu.polytech.ticket.feign.AuthFeignClientService;
import edu.polytech.ticket.kafka.TicketProducer;
import edu.polytech.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repository;
    private final TicketProducer ticketProducer;
    private final AuthFeignClientService authFeignClientService;


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


   /*public List<TicketEntity> getTicketsByProjectId(Integer projectId) {
        return repository.findByProjectId(projectId);
    }*/


    public List<TicketEntity> getTicketsByProjectSmart(Integer projectId) {
        // Fetch project details
        ProjectDto project = authFeignClientService.getProjectById(projectId);
        if (project == null || project.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with ID: " + projectId);
        }

        // Check architecture and fetch tickets accordingly
        List<Integer> projectIds;
        if (project.getArchitecture() == Architecture.MICROSERVICES) {
            projectIds = getAllProjectIdsRecursively(project);
        } else {
            projectIds = Collections.singletonList(project.getId());
        }

        return repository.findAllByProjectIdIn(projectIds);
    }

    private List<Integer> getAllProjectIdsRecursively(ProjectDto project) {
        List<Integer> ids = new ArrayList<>();
        if (project == null || project.getId() == null) {
            return ids;
        }
        ids.add(project.getId());
        List<ProjectDto> children = project.getMicroservices();
        if (children != null) {
            for (ProjectDto child : children) {
                ids.addAll(getAllProjectIdsRecursively(child));
            }
        }
        return ids;
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


    /*public List<TicketEntity> getTicketsByProjectIdAndUserId(Integer projectId, Integer userId) {
        return repository.findByProjectIdAndAssignedUserId(projectId, userId);
    }*/

    public List<TicketEntity> filterTickets(Integer projectId, String category, String assignedUserName) {
        List<TicketEntity> tickets = repository.findByProjectId(projectId);

        return tickets.stream()
                .filter(ticket -> category == null || category.equalsIgnoreCase(ticket.getCategory()))
                .filter(ticket -> {
                    if (assignedUserName == null) return true;

                    Integer assignedId = ticket.getAssignedUserId();
                    if (assignedId == null) return false;

                    try {
                        UserDto user = authFeignClientService.getUserById(assignedId);
                        String name = user.getName();
                        return name.equalsIgnoreCase(assignedUserName);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();
    }



    public TicketDto updateTicketStatus(Integer ticketId, Status newStatus) {
        TicketEntity ticket = repository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));

        ticket.setStatus(newStatus);
        TicketEntity saved = repository.save(ticket);
        return toDto(saved);
    }


    public TicketDto updateTicketPriority(Integer ticketId, Priority newPriority) {
        TicketEntity ticket = repository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));

        ticket.setPriority(newPriority);
        TicketEntity saved = repository.save(ticket);
        return toDto(saved);
    }


    public List<TicketEntity> findTicketsByCategory(String category) {
        return repository.findByCategory(category);
    }

}
