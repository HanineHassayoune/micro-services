package edu.polytech.ticket.controller;

import edu.polytech.ticket.dto.CommentDto;
import edu.polytech.ticket.entity.CommentEntity;
import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.service.CommentService;
import edu.polytech.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final TicketService ticketService;

    @PostMapping("/ticket/{ticketId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCommentToTicket(
            @PathVariable Integer ticketId,
            @RequestBody CommentEntity comment,
            @RequestParam(required = false) Integer parentCommentId
    ) {
        TicketEntity ticket = ticketService.findTicketById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        comment.setTicket(ticket);

        if (parentCommentId != null) {
            CommentEntity parent = commentService.findById(parentCommentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment not found"));
            comment.setParentComment(parent);
        }

        commentService.saveComment(comment);
    }


    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<CommentDto>> getCommentsByTicket(@PathVariable Integer ticketId) {
        List<CommentDto> comments = commentService.findByTicketId(ticketId);
        return ResponseEntity.ok(comments);
    }
}
