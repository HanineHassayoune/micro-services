package edu.polytech.ticket.dto;


import edu.polytech.ticket.enums.Priority;
import edu.polytech.ticket.enums.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    private Integer id;
    private Status status;
    private String title;
    private Priority priority;
    private String imageUrl;
    private String level;
    private String date;
    private String projectName;
    private String loggerName;
    private String category;
    private Integer projectId;
    private String stackTrace;
    private Integer assignedUserId;


}








