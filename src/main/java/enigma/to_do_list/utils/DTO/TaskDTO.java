package enigma.to_do_list.utils.DTO;

import lombok.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskDTO {
    private String taskList;
    private String taskDetail;
    private Date taskDate;
    private Date dueDate;
    private boolean completed;
}
