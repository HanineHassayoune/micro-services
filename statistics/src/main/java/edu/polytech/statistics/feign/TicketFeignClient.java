package edu.polytech.statistics.feign;

import edu.polytech.statistics.dto.TicketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "tickets")
public interface TicketFeignClient {
    @GetMapping("/api/v1/tickets/by-project/{projectId}")
    List<TicketDto> getTicketsByProject(@PathVariable  Integer projectId);

    @GetMapping("/api/v1/tickets/statuses/{projectId}")
    Map<String, Long> getTicketStatusesByProject(@PathVariable Integer projectId);

    @GetMapping("/api/v1/tickets/categories/percentages/{projectId}")
    Map<String, Double> getTicketCategoriesPercentageByProject(@PathVariable Integer projectId);

    @GetMapping("/api/v1/tickets/priorities/{projectId}")
    Map<String, Long> getTicketPrioritiesByProject(@PathVariable Integer projectId);

    @GetMapping("/api/v1/tickets/count-by-category-priority/{projectId}")
    Map<String, Map<String, Long>> getTicketCountByCategoryAndPriority(@PathVariable Integer projectId);

    @GetMapping("/api/v1/tickets/count-per-day")
    Map<String, Long> countTicketsPerDay(@RequestParam("projectId") Integer projectId);

}
