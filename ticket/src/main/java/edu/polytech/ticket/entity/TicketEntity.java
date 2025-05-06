package edu.polytech.ticket.entity;


import jakarta.persistence.*;
import lombok.*;



import java.util.List;
import java.util.UUID;

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
    //private String description; // tasks: Array
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private String imageUrl;
    private String level;
    private String date;
    private String projectName;
    private String loggerName;
    private String type;
    @Column(name = "project_id")
    private Integer projectId;

    @Column(columnDefinition = "TEXT")
    private String stackTrace;


    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL,orphanRemoval = true)
    private SolutionEntity solution;

    //@ManyToOne
    //@JoinColumn(name = "project_id")
    //private Project project;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

}
