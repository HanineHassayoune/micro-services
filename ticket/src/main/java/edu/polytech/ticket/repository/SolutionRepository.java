package edu.polytech.ticket.repository;

import edu.polytech.ticket.entity.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolutionRepository extends JpaRepository<SolutionEntity, Integer> {
    Optional<SolutionEntity> findByTicketId(Integer ticketId);


}
