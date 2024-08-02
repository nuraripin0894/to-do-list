package enigma.to_do_list.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {
    @GetMapping
    public ResponseEntity<?> home(){
        return new ResponseEntity<>("Nginx load balance test", HttpStatus.OK);
    }
}
