package edu.polytech.ticket.service;

import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.kafka.TicketProducer;
import edu.polytech.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repository;
    private final TicketProducer ticketProducer;

    public void saveTicket(TicketEntity ticket){
        repository.save(ticket);
        //send ticket to kafka
        ticketProducer.sendTicket(ticket);
        System.out.println("Ticket enregistré et envoyé à Kafka : " + ticket.getTitle());    }

    public List<TicketEntity>findAllTickets(){
        return repository.findAll();
    }


}
