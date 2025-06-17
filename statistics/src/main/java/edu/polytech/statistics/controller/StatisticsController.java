package edu.polytech.statistics.controller;

import edu.polytech.statistics.dto.ProjectDto;
import edu.polytech.statistics.feign.AuthFeignClientService;
import edu.polytech.statistics.feign.TicketFeignClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {


    private final AuthFeignClientService authFeignClientService;
    private final TicketFeignClientService ticketFeignClientService;

    @GetMapping("/projects")
    public List<ProjectDto> getAllProjects(@RequestHeader("Authorization") String token) {
        return authFeignClientService.getAllProjects(token);
    }

    @GetMapping("/tickets/status/{projectId}")
    public Map<String, Long> getTicketsStatusByProject(@PathVariable Integer projectId) {
        return ticketFeignClientService.getTicketStatusesByProject(projectId);
    }

    @GetMapping("/tickets/categories/percent/{projectId}")
    public Map<String, Double> getTicketCategoriesPercentage(@PathVariable Integer projectId) {
        return ticketFeignClientService.getTicketCategoriesPercentageByProject(projectId);
    }

    @GetMapping("/users/count/{projectId}")
    public int getUserCountByProject(@PathVariable Integer projectId) {
        return authFeignClientService.getUserCountByProject(projectId);
    }

    @GetMapping("/tickets/priorities/{projectId}")
    public Map<String, Long> getTicketPrioritiesByProject(@PathVariable Integer projectId) {
        return ticketFeignClientService.getTicketPrioritiesByProject(projectId);
    }


    @GetMapping("/tickets/count-category-priority/{projectId}")
    public Map<String, Map<String, Long>> getCountTicketsByCategoryAndPriority(@PathVariable Integer projectId) {
        return ticketFeignClientService.getTicketCountByCategoryAndPriority(projectId);
    }


}
