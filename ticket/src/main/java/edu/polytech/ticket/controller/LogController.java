package edu.polytech.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.polytech.ticket.dto.LogErrorDto;
import edu.polytech.ticket.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

   /* @PostMapping
    public ResponseEntity<String> receiveLog() {
        System.out.println("✅ RECEIVED LOG DATA");
        return ResponseEntity.ok("Log received");
    }*/

    /*@PostMapping(consumes = {"application/json", "application/x-ndjson"})
    public ResponseEntity<String> receiveLog(@RequestBody String rawLog) {
        System.out.println("✅ RECEIVED NDJSON LOG: " + rawLog);
        return ResponseEntity.ok("received");
    }*/


    @PostMapping(consumes = {"application/json", "application/x-ndjson"})
    public ResponseEntity<String> receiveLog(@RequestBody String rawLog) {
        System.out.println("✅ RECEIVED NDJSON LOG: " + rawLog);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> logMap = mapper.readValue(rawLog, Map.class);

            LogErrorDto dto = LogErrorDto.builder()
                    .message((String) logMap.get("message"))
                    .loggerName((String) logMap.get("logger_name"))
                    .threadName((String) logMap.get("thread_name"))
                    .level((String) logMap.get("level"))
                    .timestamp((String) logMap.get("@timestamp"))
                    .containerId((String) logMap.get("container_id"))
                    .containerName((String) logMap.get("container_name"))
                    .category((String) logMap.get("category"))
                    .stackTrace((String) logMap.get("stack_trace"))
                    .projectName((String) logMap.get("projectName"))
                    .build();

            logService.saveLog(dto);
            return ResponseEntity.ok("Log saved successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to parse or save log");
        }
    }
}