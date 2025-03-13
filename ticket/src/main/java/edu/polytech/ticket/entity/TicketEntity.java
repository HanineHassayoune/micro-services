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
    private UUID uuid;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String title;
    private String description; // tasks: Array
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private String imageUrl;
    private String category;
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL,orphanRemoval = true)
    private SolutionEntity solution;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

}
