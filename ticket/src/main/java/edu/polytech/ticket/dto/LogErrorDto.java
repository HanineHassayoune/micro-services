package edu.polytech.ticket.dto;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogErrorDto {
    private String message;
    private String loggerName;
    private String threadName;
    private String level;
    private String timestamp;
    private String containerId;
    private String containerName;
    private String projectName;
    private String category;
    private String stackTrace;


}
