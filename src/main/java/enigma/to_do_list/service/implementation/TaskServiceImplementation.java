package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.repository.TaskRepository;
import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.service.UserEntityService;
import enigma.to_do_list.utils.DTO.TaskDTO;
import enigma.to_do_list.utils.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class TaskServiceImplementation implements TaskService {
    private final TaskRepository taskRepository;
    private final UserEntityService userEntityService;

    @Override
    public Task create(TaskDTO request) {
        UserEntity userEntity = userEntityService.getOne(request.getUser_id());
        Task task = new Task();
        task.setUser(userEntity);
        task.setTaskList(request.getTaskList());
        task.setTaskDetail(request.getTaskDetail());
        task.setTaskDate(request.getTaskDate());
        task.setDueDate(request.getDueDate());
        task.setCompleted(false);
        Format f = new SimpleDateFormat("EEEE");
        String day = f.format(request.getTaskDate());
        task.setDayOfTask(day);
        return taskRepository.save(task);
    }

    @Override
    public Page<Task> getAll(Pageable pageable, String dayOfTask, Integer id)  {
        Specification<Task> spec = TaskSpecification.getSpecification(dayOfTask, id);
        return taskRepository.findAll(spec, pageable);
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
            task.setUser(userEntityService.getOne(request.getUser_id()) != null ? userEntityService.getOne(request.getUser_id()) : task.getUser());
            task.setTaskList(request.getTaskList() != null ? request.getTaskList() : task.getTaskList());
            task.setTaskDetail(request.getTaskDetail() != null ? request.getTaskDetail() : task.getTaskDetail());
            task.setTaskDate(request.getTaskDate() != null ? request.getTaskDate() : task.getTaskDate());
            task.setDueDate(request.getDueDate() != null ? request.getDueDate() : task.getDueDate());
            task.setCompleted(request.isCompleted() != false ? request.isCompleted() : task.isCompleted());
            if(request.getTaskDate() != null) {
                Format f = new SimpleDateFormat("EEEE");
                String day = f.format(request.getTaskDate());
                task.setDayOfTask(day);
            } else{
                task.setDayOfTask(task.getDayOfTask());
            }
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
