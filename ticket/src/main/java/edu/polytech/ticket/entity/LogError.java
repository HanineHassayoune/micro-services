package edu.polytech.ticket.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String loggerName;
    private String threadName;
    private String level;
    private String timestamp;
    private String containerId;
    private String containerName;
    private String projectName;
    private String category;
    @Column(columnDefinition = "TEXT")
    private String stackTrace;

}
