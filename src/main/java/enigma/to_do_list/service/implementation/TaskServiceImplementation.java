package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.model.taskStatus;
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
import org.springframework.stereotype.Service;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TaskServiceImplementation implements TaskService {
    private final TaskRepository taskRepository;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public Task create(TaskDTO request, String token) {
        token = token.substring(7);
        Integer id = jwtUtils.extractUserId(token);
        Task task = new Task();
        task.setUser(userEntityRepository.findById(id).get());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        LocalDate createdAt = LocalDate.now();
        task.setCreatedAt(createdAt);
        task.setDueDate(request.getDueDate());
        task.setStatus(taskStatus.PENDING);
        Format f = new SimpleDateFormat("EEEE");
        String day = f.format(request.getDueDate());
        task.setDayOfDueDate(day);
        return taskRepository.save(task);
    }

    @Override
    public Page<Task> getAll(Pageable pageable, String sortBy, boolean ascending, String status, String token)  {
        token = token.substring(7);
        Integer id = jwtUtils.extractUserId(token);
        UserEntity user = userEntityRepository.getOne(id);
        if(user.getRole().equals("ADMIN")) {
            id = null;
        }
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Specification<Task> spec = TaskSpecification.getSpecification(status, id);
        return taskRepository.findAll(spec, sortedPageable);
    }

    @Override
    public Task getOne(Integer id) {
        return taskRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Task with id " + id + " not found!"));
    }

    @Override
    public Task update(Integer id, TaskDTO request) {
        if(taskRepository.findById(id).isEmpty()){
            throw new RuntimeException("Task with id " + id + " not found!");
        }
        else {
            Task task = this.getOne(id);
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
    }

    @Override
    public Task updateStatus(Integer id, TaskDTO request) {
        if(taskRepository.findById(id).isEmpty()){
            throw new RuntimeException("Task with id " + id + " not found!");
        }
        else {
            Task task = this.getOne(id);
            task.setTitle(task.getTitle());
            task.setDescription(task.getDescription());
            task.setStatus(request.getStatus() != null ? request.getStatus() : task.getStatus());
            task.setDueDate(task.getDueDate());
            return taskRepository.save(task);
        }
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
