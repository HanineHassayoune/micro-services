
package edu.polytech.ticket.dto;

import edu.polytech.ticket.enums.Architecture;
import lombok.Data;

import java.util.List;

@Data
public class ProjectDto {
    private Integer id;
    private String title;
    private Architecture architecture;
    private List<ProjectDto> microservices;
    private List<UserDto> users;


}
