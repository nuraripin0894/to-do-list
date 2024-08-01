package enigma.to_do_list.controller;

import enigma.to_do_list.exception.CustomUserException;
import enigma.to_do_list.exception.ErrorResponse;
import enigma.to_do_list.exception.Response;
import enigma.to_do_list.utils.Res;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

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
        if(message.contains("Task title is required!") && message.contains("model.Task")){
            message = "Task title cannot be blank!";
        }
        if(message.contains("Task due date is required!")){
            message = "Task due date cannot be blank!";
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
        if(message.contains("Bad credentials") || message.contains("UserDetailsService returned null")){
            message = "Invalid e-mail or password!";
        }
        if(message.contains("as a Date")){
            message = "Please enter the date in the specified format!";
        }
        if(message.contains("Please log in again.")){
            message = "Your token is invalid or expired. Please log in again.";
        }
        if(message.contains("Key (email)")){
            message = "E-mail already exist, please use different E-mail.";
        }
        if(message.contains("Key (username)")){
            message = "Username already exist, please use different username.";
        }
        return Res.renderJson(null, message, status);
    }

    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<ErrorResponse> handleCustomUserException(CustomUserException e) {
        return Response.error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(HttpClientErrorException.Unauthorized e) {
        return Response.error(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(HttpClientErrorException.Forbidden e) {
        return Response.error(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpClientErrorException.NotFound e) {
        return Response.error(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(HttpServerErrorException.InternalServerError e) {
        return Response.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
