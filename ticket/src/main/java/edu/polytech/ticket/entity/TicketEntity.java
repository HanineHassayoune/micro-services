package edu.polytech.ticket.entity;


import edu.polytech.ticket.enums.Priority;
import edu.polytech.ticket.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketEntity {
    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String title;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private String level;
    private String date;
    private String projectName;
    private String loggerName;
    private String category;
    @Column(name = "project_id")
    private Integer projectId;

    @Column(columnDefinition = "TEXT")
    private String stackTrace;


    @Column(name = "branch_name")
    private String branchName;


    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL,orphanRemoval = true)
    private SolutionEntity solution;


    @Column(name = "assigned_user_id")
    private Integer assignedUserId;


    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

}
