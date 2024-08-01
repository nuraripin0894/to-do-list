package enigma.to_do_list.controller;

import enigma.to_do_list.exception.Response;
import enigma.to_do_list.model.Task;
import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.utils.DTO.TaskDTO;
import enigma.to_do_list.utils.PageResponWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Cacheable
    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskDTO request,
                                    Authentication auth){
        Task result = taskService.create(request, auth);
        return Response.success(
                result,
                "Data Has Been Created!",
                HttpStatus.CREATED
        );
    }

    @Cacheable
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            Authentication auth
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDTO> res = taskService.getAll(pageable, sortBy, ascending, status, auth);
        PageResponWrapper<TaskDTO> result = new PageResponWrapper<>(res);
        return Response.success(
                result,
                result.getTotalElements() == 0 ? "Data Empty!" : "Data Found!",
                HttpStatus.OK
        );
    }

    @Cacheable
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id, Authentication auth){
        TaskDTO result = taskService.getOne(id, auth);
        return Response.success(
                result,
                "Data Found!",
                HttpStatus.OK
        );
    }

    @Cacheable
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody TaskDTO request){
        Task result = taskService.update(id, request);
        return Response.success(
                result,
                "Data Has Been Updated!",
                HttpStatus.OK
        );
    }

    @Cacheable
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody TaskDTO request){
        Task result = taskService.updateStatus(id, request);
        return Response.success(
                result,
                "Status Has Been Updated!",
                HttpStatus.OK
        );
    }

    @Cacheable
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        taskService.delete(id);
        return Response.success(
                null,
                "Data Has Been Deleted!",
                HttpStatus.NO_CONTENT
        );
    }
}
