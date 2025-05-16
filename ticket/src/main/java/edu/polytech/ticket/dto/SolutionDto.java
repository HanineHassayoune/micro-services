package edu.polytech.ticket.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolutionDto {
    private Integer id;
    private String description;
    private String reference;
    private String code;
    private Integer ticketId;
    private Integer userId;
    private LocalDateTime datePosted; }
