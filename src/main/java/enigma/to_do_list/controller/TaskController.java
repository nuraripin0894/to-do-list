package enigma.to_do_list.controller;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.utils.DTO.TaskDTO;
import enigma.to_do_list.utils.PageResponWrapper;
import enigma.to_do_list.utils.Res;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskDTO request,
                                    @RequestHeader("Authorization") String token){
        Task result = taskService.create(request, token);
        return Res.renderJson(
                result,
                "Data Has Been Created!",
                HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String dayOfTask,
            @RequestHeader("Authorization") String token
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> res = taskService.getAll(pageable, dayOfTask, token);
        PageResponWrapper<Task> result = new PageResponWrapper<>(res);
        return Res.renderJson(
                result,
                result.getTotalElements() == 0 ? "Data Empty!" : "Data Found!",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        Task result = taskService.getOne(id);
        return Res.renderJson(
                result,
                "Data Found!",
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody TaskDTO request){
        Task result = taskService.update(id, request);
        return Res.renderJson(
                result,
                "Data Has Been Updated!",
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        taskService.delete(id);
        return Res.renderJson(
                null,
                "Data Has Been Deleted!",
                HttpStatus.OK
        );
    }
}
