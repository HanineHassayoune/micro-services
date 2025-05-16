package edu.polytech.ticket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolutionEntity {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    private String reference;

    private String code;

    @OneToOne
    @JoinColumn(name = "ticket_id", unique = true)
    private TicketEntity ticket;


    @Column(name = "created_by_user_id")
    private Integer createdByUserId;

    @CreationTimestamp
    @Column(name = "date_posted")
    private LocalDateTime datePosted;

}
