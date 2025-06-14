package edu.polytech.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatisticsDto {
    private Integer projectId;
    private String projectTitle;
    private Integer userCount;
    private Map<String, Integer> ticketStatusCount;  // ex: { "TODO": 10, "DONE": 5 }
    private Map<String, Integer> ticketCategoryCount; // ex: { "Bug": 7, "Feature": 8 }
}
