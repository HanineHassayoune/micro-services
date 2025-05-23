package edu.polytech.ticket.dto;

import edu.polytech.ticket.enums.Role;
import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private String profileImage;
}
