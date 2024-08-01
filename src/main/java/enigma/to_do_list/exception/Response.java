package enigma.to_do_list.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    public static <T> ResponseEntity<SuccessResponse<T>> success(T data, String message, HttpStatus status) {
        SuccessResponse<T> response = SuccessResponse.<T>builder()
                .status(status.value())
                .data(data)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public static <T> ResponseEntity<?> error(RuntimeException e, String message, HttpStatus status) {
        ErrorResponse<T> response = ErrorResponse.<T>builder()
                .message(e.getMessage())
                .status(status.getReasonPhrase())
                .error(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
