package enigma.to_do_list.controller;

import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.service.UserEntityService;
import enigma.to_do_list.utils.PageResponWrapper;
import enigma.to_do_list.utils.Res;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEntityController {
    private final UserEntityService userEntityService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserEntity request){
        UserEntity result = userEntityService.create(request);
        return Res.renderJson(
                result,
                "Data Has Been Created!",
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String username
    ) {
        Page<UserEntity> res = userEntityService.getAll(pageable, username);
        PageResponWrapper<UserEntity> result = new PageResponWrapper<>(res);
        return Res.renderJson(
                result,
                result.getTotalElements() == 0 ? "Data Empty!" : "Data Found!",
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        UserEntity result = userEntityService.getOne(id);
        return Res.renderJson(
                result,
                "Data Found!",
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody UserEntity request){
        UserEntity result = userEntityService.update(id, request);
        return Res.renderJson(
                result,
                "Data Has Been Updated!",
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        userEntityService.delete(id);
        return Res.renderJson(
                null,
                "Data Has Been Deleted!",
                HttpStatus.OK
        );
    }
}
