package edu.polytech.ticket.repository;

import edu.polytech.ticket.entity.TicketEntity;
import edu.polytech.ticket.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {
    List<TicketEntity> findByProjectId(Integer projectId);

    List<TicketEntity> findByCategoryAndStatus(String category, Status status);


    List<TicketEntity> findAllByProjectIdIn(List<Integer> projectIds);


}

