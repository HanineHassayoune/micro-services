package edu.polytech.ticket.service;

import edu.polytech.ticket.dto.TicketDto;
import edu.polytech.ticket.dto.ProjectDto;
import edu.polytech.ticket.dto.UserDto;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.enums.Architecture;
import edu.polytech.ticket.enums.Priority;
import edu.polytech.ticket.enums.Status;
import edu.polytech.ticket.feign.AuthFeignClientService;
import edu.polytech.ticket.gitHub.GitHubService;
import edu.polytech.ticket.kafka.TicketProducer;
import edu.polytech.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repository;
    private final TicketProducer ticketProducer;
    private final AuthFeignClientService authFeignClientService;
    private final GitHubService gitHubService;

    public void saveTicket(TicketEntity ticket) {
        try {
            ProjectDto project = authFeignClientService.getProjectByTitle(ticket.getProjectName());

            if (project != null && project.getId() != null) {
                ticket.setProjectId(project.getId());
                TicketEntity saved = repository.save(ticket); // on récupère l'objet sauvegardé

                // Créer la branche GitHub liée au ticket
                String branchName = "ticket_" + saved.getId();
                try {
                    gitHubService.createBranchForTicket(ticket.getProjectName(), branchName);
                    // Mettre à jour le ticket avec le nom de la branche
                    saved.setBranchName(branchName);
                    repository.save(saved);

                } catch (Exception ex) {
                    System.err.println("Échec de la création de la branche GitHub : " + ex.getMessage());
                }

                // Send ticket to Kafka
                UserDto manager = authFeignClientService.getManagerByProject(ticket.getProjectName());
                System.out.println("Send ticket to Kafka with managerId=" + manager.getId());
                ticketProducer.sendTicket(saved, manager.getId());

            } else {
                System.err.println(" Projet found but ID nul : " + ticket.getProjectName());
            }
        } catch (Exception e) {
            System.err.println("Projet not found or ou erreur lors de la récupération : " + ticket.getProjectName());
            e.printStackTrace();
        }
    }


    public void updateTicket(TicketEntity ticket) {
        repository.save(ticket); //  save sans appel Kafka
    }

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


    public List<TicketEntity> findDoneTicketsByCategory(String category) {
        return repository.findByCategoryAndStatus(category, Status.DONE);
    }

    public Map<String, Long> countTicketsByStatus(Integer projectId) {
        return repository.findByProjectId(projectId).stream()
                .collect(Collectors.groupingBy(
                        ticket -> ticket.getStatus().name(),
                        Collectors.counting()
                ));
    }

    public Map<String, Double> getTicketCategoriesPercentage(Integer projectId) {
        List<TicketEntity> tickets = repository.findByProjectId(projectId);
        long total = tickets.size();

        if (total == 0) return Map.of();

        return tickets.stream()
                .collect(Collectors.groupingBy(
                        TicketEntity::getCategory,
                        Collectors.collectingAndThen(Collectors.counting(), count -> (count * 100.0) / total)
                ));
    }


    public Map<String, Long> countTicketsByPriority(Integer projectId) {
        List<TicketEntity> tickets = getTicketsByProjectSmart(projectId);

        Map<String, Long> counts = tickets.stream()
                .collect(Collectors.groupingBy(
                        ticket -> ticket.getPriority().name(),
                        Collectors.counting()
                ));
        // Assurer que toutes les priorités sont présentes
        for (Priority priority : Priority.values()) {
            counts.putIfAbsent(priority.name(), 0L);
        }

        return counts;
    }

    public Map<String, Map<String, Long>> countTicketsByCategoryAndPriority(Integer projectId) {
        List<TicketEntity> tickets = repository.findByProjectId(projectId);

        // Grouper par catégorie puis par priorité
        Map<String, Map<String, Long>> result = tickets.stream()
                .filter(ticket -> ticket.getCategory() != null && !ticket.getCategory().isBlank()) // exclure catégorie null ou vide
                .collect(Collectors.groupingBy(
                        TicketEntity::getCategory,
                        Collectors.groupingBy(
                                ticket -> ticket.getPriority().name(),
                                Collectors.counting()
                        )
                ));
        // Assurer que toutes les priorités existent pour chaque catégorie, même à 0
        for (String category : result.keySet()) {
            Map<String, Long> priorityMap = result.get(category);
            for (Priority p : Priority.values()) {
                priorityMap.putIfAbsent(p.name(), 0L);
            }
        }

        return result;
    }

    public Map<String, Long> countTicketsPerDayByProject(Integer projectId) {
        List<TicketEntity> tickets = repository.findByProjectId(projectId);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        return tickets.stream()
                .filter(t -> t.getDate() != null)
                .collect(Collectors.groupingBy(
                        t -> {
                            try {
                                OffsetDateTime odt = OffsetDateTime.parse(t.getDate(), formatter);
                                return odt.toLocalDate().toString(); // format "2025-06-17"
                            } catch (Exception e) {
                                return "invalid-date";
                            }
                        },
                        Collectors.counting()
                ));
    }


}
