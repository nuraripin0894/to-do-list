package enigma.to_do_list.utils.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import enigma.to_do_list.model.TaskStatus;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private Date dueDate;
    private TaskStatus status;
    private LocalDate createdAt;
}
