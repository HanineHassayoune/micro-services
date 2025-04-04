package edu.polytech.ticket.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;



@FeignClient(name = "authentications")
public interface AuthFeignClient {
    @GetMapping("/api/v1/auth/validate-token")
    Boolean validateToken(@RequestHeader("Authorization") String authorizationHeader);
}

