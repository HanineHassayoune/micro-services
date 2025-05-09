package edu.polytech.ticket.service;

import edu.polytech.ticket.dto.CommentDto;
import edu.polytech.ticket.entity.CommentEntity;
import edu.polytech.ticket.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Optional<CommentEntity> findById(Integer id) {
        return repository.findById(id);
    }

    public List<CommentDto> findByTicketId(Integer ticketId) {
        List<CommentEntity> comments = repository.findByTicketId(ticketId);
        return comments.stream().map(this::toDto).collect(Collectors.toList());
    }

    public CommentDto toDto(CommentEntity entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .author(entity.getAuthor() != null ? entity.getAuthor() : "Unknown")
                .date(entity.getDate())
                .reaction(entity.getReaction())
                .ticketId(entity.getTicket().getId())
                .build();
    }


}
