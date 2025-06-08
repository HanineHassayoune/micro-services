package edu.polytech.notification.dto;

import edu.polytech.notification.enums.Priority;
import edu.polytech.notification.enums.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    private Integer id;
    private String title;
   private String projectName;
   private Integer projectId;
    private Integer managerId;

}
