package enigma.to_do_list.utils.DTO;

import enigma.to_do_list.model.taskStatus;

import lombok.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskDTO {
    private String title;
    private String description;
    private Date dueDate;
    private taskStatus status;
}
