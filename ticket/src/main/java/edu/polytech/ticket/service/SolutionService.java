package edu.polytech.ticket.service;

import edu.polytech.ticket.entity.SolutionEntity;
import edu.polytech.ticket.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolutionService {
    private final SolutionRepository repository;

    public void saveSolution(SolutionEntity solution){
        repository.save(solution);
    }

    public List<SolutionEntity> findAllSolutions(){
        return repository.findAll();
    }
}





