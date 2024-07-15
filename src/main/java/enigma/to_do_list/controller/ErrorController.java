package enigma.to_do_list.controller;

import enigma.to_do_list.utils.Res;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e){
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if(message.contains("Name is required!") && message.contains("model.UserEntity")){
            message = "Username cannot be blank!";
        }
        if(message.contains("E-mail is required!") && message.contains("model.UserEntity")){
            message = "E-mail cannot be blank!";
        }
        if(message.contains("E-mail is not valid!") && message.contains("model.UserEntity")){
            message = "E-mail is not valid!";
        }
        if(message.contains("Password is required") && message.contains("model.UserEntity")){
            message = "Password cannot be blank!";
        }
        if(message.contains("user_id is required!") && message.contains("model.Task")){
            message = "User ID cannot be blank!";
        }
        if(message.contains("task list is required!") && message.contains("model.Task")){
            message = "Task list cannot be blank!";
        }
        if(message.contains("task date is required!")){
            message = "Task date cannot be blank!";
        }

        return Res.renderJson(null, message, status);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e){
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if(message.contains("User with id")){
            message = "User not found!";
        }
        if(message.contains("Task with id")){
            message = "Task not found!";
        }
        if(message.contains("request body is missing") && message.contains("model.UserEntity")){
            message = "Please fill data to update!";
        }
        if(message.contains("not strong enough!")){
            message = "Password is not strong enough! " +
                    "(password must contain number, symbol, upper & lower case, " +
                    "should longer than 8 character (20 at max)";
        }
        if(message.contains("Bad credentials")){
            message = "Invalid e-mail or password!";
        }


        return Res.renderJson(null, message, status);
    }
}
