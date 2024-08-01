package enigma.to_do_list.service;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.utils.DTO.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Task create(TaskDTO request, String token);
    Page<Task> getAll(Pageable pageable, String sortBy, boolean ascending, String status, String token);
    Task getOne(Integer id);
    Task update(Integer id, TaskDTO request);
    Task updateStatus(Integer id, TaskDTO request);
    void delete(Integer id);
}
