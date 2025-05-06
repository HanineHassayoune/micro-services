package edu.polytech.ticket.dto;


import edu.polytech.ticket.entity.Priority;
import edu.polytech.ticket.entity.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogTicketDto {
    private Status status;
    private String title;
    private Priority priority;
    //private String description;
    private String level;
    private String date;
    private String projectName;
    private String loggerName;
    private String stackTrace;
    private String type;


}








