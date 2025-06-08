package edu.polytech.notification.feign;

import org.springframework.stereotype.Service;

@Service
public class AuthFeignClientService {
    private final AuthFeignClient authFeignClient;

    public AuthFeignClientService(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    public Integer extractUserIdFromToken(String token) {
        return authFeignClient.getCurrentUser(token).getId();
    }
}
