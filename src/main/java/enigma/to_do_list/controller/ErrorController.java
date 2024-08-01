package enigma.to_do_list.controller;

import enigma.to_do_list.exception.Response;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e){
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if(message.contains("Name is required!") && message.contains("model.UserEntity")){
            message = "Username cannot be blank!";
        }
        if(message.contains("Email is required!") && message.contains("model.UserEntity")){
            message = "Email cannot be blank!";
        }
        if(message.contains("Email is not valid!") && message.contains("model.UserEntity")){
            message = "Email is not valid!";
        }
        if(message.contains("Password is required") && message.contains("model.UserEntity")){
            message = "Password cannot be blank!";
        }
        if(message.contains("user_id is required!") && message.contains("model.Task")){
            message = "User ID cannot be blank!";
        }
        if(message.contains("Task title is required!") && message.contains("model.Task")){
            message = "Task title cannot be blank!";
        }
        if(message.contains("Task due date is required!")){
            message = "Task due date cannot be blank!";
        }

        return Response.error(e, message, status);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e){
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if(message.contains("user with id")){
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
                    "should longer than 12 character (20 at max)";
        }
        if(message.contains("Bad credentials") || message.contains("UserDetailsService returned null")){
            message = "Invalid email or password!";
        }
        if(message.contains("as a Date")){
            message = "Please enter the date in the specified format!";
        }
        if(message.contains("Please log in again.")){
            message = "Your token is invalid or expired. Please log in again.";
        }
        if(message.contains("Key (email)")){
            message = "Email already exist, please use different email.";
        }
        if(message.contains("Key (username)")){
            message = "Username already exist, please use different username.";
        }
        if(message.contains("email login")){
            message = "Email is required to login.";
        }
        if(message.contains("email is not valid")){
            message = "Email is not valid!.";
        }
        if(message.contains("password login")){
            message = "Password is required to login.";
        }
        if(message.contains("email not found")){
            status = HttpStatus.UNAUTHORIZED;
            message = "Invalid credentials, email is not found";
        }
        if(message.contains("password not match")){
            status = HttpStatus.UNAUTHORIZED;
            message = "Invalid credentials, password not match";
        }
        return Response.error(e, message, status);
    }
}
