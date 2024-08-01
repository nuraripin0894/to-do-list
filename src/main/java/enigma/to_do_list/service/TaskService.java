package enigma.to_do_list.service;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.utils.DTO.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface TaskService {
    Task create(TaskDTO request, Authentication auth);
    Page<TaskDTO> getAll(Pageable pageable, String sortBy, boolean ascending, String status, Authentication auth);
    TaskDTO getOne(Integer id, Authentication auth);
    Task update(Integer id, TaskDTO request);
    Task updateStatus(Integer id, TaskDTO request);
    void delete(Integer id);
}
