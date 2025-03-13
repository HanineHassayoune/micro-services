package edu.polytech.ticket.controller;

import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody TicketEntity ticket){

        service.saveTicket(ticket);
    }


    @GetMapping
    public ResponseEntity<List<TicketEntity>> findAllTickets(){

        return ResponseEntity.ok(service.findAllTickets());
    }

}
