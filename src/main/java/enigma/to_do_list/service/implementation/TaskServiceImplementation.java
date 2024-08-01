package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.Roles;
import enigma.to_do_list.model.Task;
import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.model.TaskStatus;
import enigma.to_do_list.repository.TaskRepository;
import enigma.to_do_list.repository.UserEntityRepository;
import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.utils.DTO.TaskDTO;
import enigma.to_do_list.utils.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskServiceImplementation implements TaskService {
    private final TaskRepository taskRepository;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public Task create(TaskDTO request, Authentication auth) {
        UserEntity user = (UserEntity) auth.getPrincipal();
        Task task = new Task();
        task.setUser(user);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        LocalDate createdAt = LocalDate.now();
        task.setCreatedAt(createdAt);
        task.setDueDate(request.getDueDate());
        task.setStatus(TaskStatus.PENDING);
        Format f = new SimpleDateFormat("EEEE");
        String day = f.format(request.getDueDate());
        task.setDayOfDueDate(day);
        return taskRepository.save(task);
    }

    @Override
    public Page<TaskDTO> getAll(Pageable pageable, String sortBy, boolean ascending, String status, Authentication auth)  {
        UserEntity user = (UserEntity) auth.getPrincipal();
        Integer id = user.getId();
        if(user.getRole() == Roles.ADMIN || user.getRole() == Roles.SUPER_ADMIN) {
            id = null;
        }
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Specification<Task> spec = TaskSpecification.getSpecification(status, id);
        Page<Task> tasks = taskRepository.findAll(spec, sortedPageable);

        return tasks.map(task -> TaskDTO.builder()
                .id(task.getId())
                .userId(user.getRole() == Roles.USER ? null : task.getUser().getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .build());
    }

    @Override
    public TaskDTO getOne(Integer id, Authentication auth) {
        UserEntity user = (UserEntity) auth.getPrincipal();
        Task task = taskRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Task with id " + id + " not found!"));

        return TaskDTO.builder()
                .title(task.getTitle())
                .id(task.getId())
                .userId(user.getRole() == Roles.USER ? null : task.getUser().getId())
                .createdAt(task.getCreatedAt())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }

    @Override
    public Task update(Integer id, TaskDTO request) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Task with id " + id + " not found!"));
        task.setTitle(request.getTitle() != null ? request.getTitle() : task.getTitle());
        task.setDescription(request.getDescription() != null ? request.getDescription() : task.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
        if(request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
            Format f = new SimpleDateFormat("EEEE");
            String day = f.format(request.getDueDate());
            task.setDayOfDueDate(day);
        } else{
            task.setDueDate(task.getDueDate());
            task.setDayOfDueDate(task.getDayOfDueDate());
        }
        return taskRepository.save(task);
    }

    @Override
    public Task updateStatus(Integer id, TaskDTO request) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
             new RuntimeException("Task with id " + id + " not found!"));
        task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
        return taskRepository.save(task);
    }

    @Override
    public void delete(Integer id) {
        if(taskRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Task with id " + id + " not found!");
        }
        else{
            taskRepository.deleteById(id);
        }
    }
}
