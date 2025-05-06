package edu.polytech.ticket.feign;

import edu.polytech.ticket.dto.ProjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


/*@FeignClient(name = "authentications")
public interface AuthFeignClient {
    @GetMapping("/api/v1/auth/validate-token")
    Boolean validateToken(@RequestHeader("Authorization") String authorizationHeader);


    @GetMapping("/api/v1/projects/title/{title}")
    ProjectDto getProjectByTitle(@PathVariable("title") String title);


}*/


@FeignClient(name = "authentications")
public interface AuthFeignClient {

    @GetMapping("/api/v1/projects/title/{title}")
    ProjectDto getProjectByTitle(@PathVariable String title);
}



