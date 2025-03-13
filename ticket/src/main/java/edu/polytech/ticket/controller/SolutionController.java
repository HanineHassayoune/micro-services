package edu.polytech.ticket.controller;

import edu.polytech.ticket.entity.SolutionEntity;
import edu.polytech.ticket.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
public class SolutionController {
    private final SolutionService service;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody SolutionEntity solution){

        service.saveSolution(solution);
    }


    @GetMapping
    public ResponseEntity<List<SolutionEntity>> findAllSolutions(){

        return ResponseEntity.ok(service.findAllSolutions());
    }
}
