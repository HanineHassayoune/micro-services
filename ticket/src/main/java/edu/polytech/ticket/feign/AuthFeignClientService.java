package edu.polytech.ticket.feign;

import edu.polytech.ticket.dto.ProjectDto;
import edu.polytech.ticket.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class AuthFeignClientService {
    private final AuthFeignClient authFeignClient;

    public AuthFeignClientService(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    public ProjectDto getProjectByTitle(String title) {
        return authFeignClient.getProjectByTitle(title);
    }

    public Integer extractUserIdFromToken(String token) {
        return authFeignClient.getCurrentUser(token).getId();
    }

    public UserDto getUserById(Integer userId) {
        return authFeignClient.getUserById(userId);
    }

    public ProjectDto getProjectById(Integer id) {
        return authFeignClient.getProjectById(id);
    }



}
