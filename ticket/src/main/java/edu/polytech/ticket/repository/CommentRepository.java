package edu.polytech.ticket.repository;

import edu.polytech.ticket.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByTicketId(Integer ticketId);

}
