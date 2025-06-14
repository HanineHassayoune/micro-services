package edu.polytech.statistics.feign;

import edu.polytech.statistics.dto.ProjectDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthFeignClientService {

    private final AuthFeignClient authFeignClient;

    public AuthFeignClientService(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    public List<ProjectDto> getAllProjects(String token) {
        return authFeignClient.getAllProjects(token);
    }

    public int getUserCountByProject(Integer projectId) {
        return authFeignClient.getUserCountByProject(projectId);
    }
}
