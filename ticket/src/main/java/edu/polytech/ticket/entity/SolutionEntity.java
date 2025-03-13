package edu.polytech.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private UUID uuid;
    private String description;
    @OneToOne
    @JoinColumn(name = "ticket_id", unique = true)
    private TicketEntity ticket;

}
