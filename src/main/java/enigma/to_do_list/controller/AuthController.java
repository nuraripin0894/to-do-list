package enigma.to_do_list.controller;

import enigma.to_do_list.service.implementation.AuthService;
import enigma.to_do_list.utils.DTO.AuthResponDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponDTO> signUp(@RequestBody AuthResponDTO signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponDTO> signIn(@RequestBody AuthResponDTO signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
}
