package edu.polytech.notification.feign;

import edu.polytech.notification.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authentications")
public interface AuthFeignClient {
    @GetMapping("/api/v1/auth/me")
    UserDto getCurrentUser(@RequestHeader("Authorization") String authorizationHeader);
}