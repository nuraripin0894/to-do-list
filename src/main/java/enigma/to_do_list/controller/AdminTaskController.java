package enigma.to_do_list.controller;

import com.fasterxml.jackson.annotation.JsonView;
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
@RequestMapping("/api/admin/todos")
@RequiredArgsConstructor
public class AdminTaskController {
    private final TaskService taskService;

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
        return new ResponseEntity<>(
                result,
                HttpStatus.OK
        );
    }

    @Cacheable
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id, Authentication auth){
        TaskDTO result = taskService.getOne(id, auth);
        return new ResponseEntity<>(
                result,
                HttpStatus.OK
        );
    }
}
