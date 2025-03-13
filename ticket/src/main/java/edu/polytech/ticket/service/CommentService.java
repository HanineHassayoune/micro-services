package edu.polytech.ticket.service;

import edu.polytech.ticket.entity.CommentEntity;
import edu.polytech.ticket.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;

    public void saveComment(CommentEntity comment){
        repository.save(comment);
    }

    public List<CommentEntity> findAllComments(){
        return repository.findAll();
    }
}
