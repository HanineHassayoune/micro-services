package edu.polytech.ticket.repository;

import edu.polytech.ticket.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {
    List<TicketEntity> findByProjectId(Long projectId);

    List<TicketEntity> findByAssignedUserId(Integer userId);

}

