package edu.polytech.ticket.service;

import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repository;

    public void saveTicket(TicketEntity ticket){
        repository.save(ticket);
    }

    public List<TicketEntity>findAllTickets(){
        return repository.findAll();
    }
}
