package edu.polytech.ticket.service;


import edu.polytech.ticket.dto.LogErrorDto;
import edu.polytech.ticket.entity.LogError;
import edu.polytech.ticket.repository.LogErrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogErrorRepository logErrorRepository;

    public void saveLog(LogErrorDto dto) {
        LogError log = LogError.builder()
                .message(dto.getMessage())
                .loggerName(dto.getLoggerName())
                .threadName(dto.getThreadName())
                .level(dto.getLevel())
                .timestamp(dto.getTimestamp())
                .containerId(dto.getContainerId())
                .containerName(dto.getContainerName())
                .exception(dto.getException())
                .stackTrace(dto.getStackTrace())
                .projectName(dto.getProjectName())
                .build();

        logErrorRepository.save(log);
    }
}
