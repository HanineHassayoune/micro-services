package edu.polytech.ticket.controller;

import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.feign.AuthFeignClientService;
import edu.polytech.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

 private final AuthFeignClientService authFeignClientService;
    private final TicketService ticketService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody TicketEntity ticket, @RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("Token reçu: " + authorizationHeader);
        Boolean isValid = authFeignClientService.validateToken(authorizationHeader.replace("Bearer ", ""));
        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }
        ticketService.saveTicket(ticket);
        System.out.println("Ticket enregistré avec succès !");
    }



    @GetMapping
    public ResponseEntity<List<TicketEntity>> getAllTickets(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        Boolean isValid = authFeignClientService.validateToken(token);
        if (Boolean.TRUE.equals(isValid)) {
            List<TicketEntity> tickets = ticketService.findAllTickets();
            return ResponseEntity.ok(tickets);
        } else {
            // Ajout du log avant de lever l'exception
            System.err.println("Invalid token provided: " + token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }





}
