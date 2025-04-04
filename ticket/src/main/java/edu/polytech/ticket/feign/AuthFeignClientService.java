package edu.polytech.ticket.feign;

import org.springframework.stereotype.Service;

@Service
public class AuthFeignClientService {
    private final AuthFeignClient authFeignClient;

    public AuthFeignClientService(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    public Boolean validateToken(String token) {
        String authorizationHeader = "Bearer " + token;
        Boolean response = authFeignClient.validateToken(authorizationHeader);
        System.out.println("Réponse du Feign Client: " + response);
        return response;
    }



}
