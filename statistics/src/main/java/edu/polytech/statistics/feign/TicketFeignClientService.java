package edu.polytech.statistics.feign;

import edu.polytech.statistics.dto.TicketDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TicketFeignClientService {
    private final TicketFeignClient ticketFeignClient;

    public TicketFeignClientService(TicketFeignClient ticketFeignClient) {
        this.ticketFeignClient = ticketFeignClient;
    }

    public List<TicketDto> getTicketsByProject(Integer projectId) {
        return ticketFeignClient.getTicketsByProject(projectId);
    }

    public Map<String, Long> getTicketStatusesByProject(Integer projectId) {
        return ticketFeignClient.getTicketStatusesByProject(projectId);
    }

    public Map<String, Double> getTicketCategoriesPercentageByProject(Integer projectId) {
        return ticketFeignClient.getTicketCategoriesPercentageByProject(projectId);
    }

    public Map<String, Long> getTicketPrioritiesByProject(Integer projectId) {
        return ticketFeignClient.getTicketPrioritiesByProject(projectId);
    }

    public Map<String, Map<String, Long>> getTicketCountByCategoryAndPriority(Integer projectId) {
        return ticketFeignClient.getTicketCountByCategoryAndPriority(projectId);
    }
    public Map<String, Long> countTicketsPerDay(Integer projectId) {
        return ticketFeignClient.countTicketsPerDay(projectId);
    }

}
