package edu.polytech.notification.dto;




import edu.polytech.notification.enums.Role;
import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private String profileImage;
}
