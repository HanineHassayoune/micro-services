package edu.polytech.ticket.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Integer id;
    private String content;
    private String author;
    private Date date;
    private String reaction;
    private Integer ticketId;


}
