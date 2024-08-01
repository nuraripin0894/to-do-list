package enigma.to_do_list.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    public static ResponseEntity<ErrorResponse> error(String message, HttpStatus status) {
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
