package edu.polytech.ticket.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolutionDto {
    private String description;
    private String reference;
    private String code;
    private Integer ticketId;
    private Integer userId;
}
