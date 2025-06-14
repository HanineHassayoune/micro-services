package edu.polytech.statistics.feign;

import edu.polytech.statistics.dto.ProjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "authentications")
public interface AuthFeignClient {

    @GetMapping("/api/v1/projects/get")
    List<ProjectDto> getAllProjects(@RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/users/count/{projectId}")
    int getUserCountByProject(@PathVariable("projectId") Integer projectId);

}