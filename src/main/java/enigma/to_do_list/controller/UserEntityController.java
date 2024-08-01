package enigma.to_do_list.controller;

import com.fasterxml.jackson.annotation.JsonView;
import enigma.to_do_list.exception.Response;
import enigma.to_do_list.model.UserEntity;
import enigma.to_do_list.service.UserEntityService;
import enigma.to_do_list.utils.DTO.AuthResponDTO;
import enigma.to_do_list.utils.PageResponWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserEntityController {
    private final UserEntityService userEntityService;
    String superAdminCreation = "superman admin turing machine gigachad sigma";
    String adminCreation = "admin turing machine alpha beta";

    @Cacheable
    @PostMapping("/super-admin")
    public ResponseEntity<?> createSuperAdmin(
            @RequestBody AuthResponDTO request,
            @RequestHeader(value = "X-Super-Admin-Secret-Key", required = false) String superAdminSecretKey
    ){
        if (superAdminSecretKey == null || !superAdminSecretKey.equals(superAdminCreation)) {
            return Response.error(new RuntimeException("Forbidden"), "You have no access to this endpoint", HttpStatus.FORBIDDEN);
        } else {
            UserEntity result = userEntityService.createSuperAdmin(request);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
    }

    @Cacheable
    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody UserEntity request){
        UserEntity result = userEntityService.create(request);
        return Response.success(
                result,
                "Data Has Been Created!",
                HttpStatus.CREATED
        );
    }

    @Cacheable
    @GetMapping("/users")
    public ResponseEntity<?> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String username
    ) {
        Page<UserEntity> res = userEntityService.getAll(pageable, username);
        PageResponWrapper<UserEntity> result = new PageResponWrapper<>(res);
        return Response.success(
                result,
                result.getTotalElements() == 0 ? "Data Empty!" : "Data Found!",
                HttpStatus.OK
        );
    }

    @Cacheable
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id){
        UserEntity result = userEntityService.getOne(id);
        return Response.success(
                result,
                "Data Found!",
                HttpStatus.OK
        );
    }

    @Cacheable
    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody UserEntity request){
        UserEntity result = userEntityService.update(id, request);
        return Response.success(
                result,
                "Data Has Been Updated!",
                HttpStatus.OK
        );
    }

    @Cacheable
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        userEntityService.delete(id);
        return Response.success(
                null,
                "Data Has Been Deleted!",
                HttpStatus.OK
        );
    }

    @Cacheable
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> changeRole(
            @PathVariable Integer id,
            @RequestHeader(value = "X-Admin-Secret-Key", required = false) String adminSecretKey,
            @RequestBody UserEntity request
    ){
        if (adminSecretKey == null || !adminSecretKey.equals(adminCreation)) {
            return Response.error(new RuntimeException("Forbidden"), "You have no access to this endpoint", HttpStatus.FORBIDDEN);
        } else {
            UserEntity result = userEntityService.changeRole(id, request);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
    }
}
