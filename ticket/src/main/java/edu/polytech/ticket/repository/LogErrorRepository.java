package edu.polytech.ticket.repository;


import edu.polytech.ticket.entity.LogError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogErrorRepository extends JpaRepository<LogError, Long> {
}