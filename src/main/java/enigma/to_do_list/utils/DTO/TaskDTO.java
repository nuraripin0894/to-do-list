package enigma.to_do_list.utils.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskDTO {
    @NotNull
    private Integer user_id;
    private String taskList;
    private String taskDetail;
    private Date taskDate;
    private Date dueDate;
    private boolean completed;
}
