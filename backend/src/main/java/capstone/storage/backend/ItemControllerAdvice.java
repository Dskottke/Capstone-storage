package capstone.storage.backend;

import capstone.storage.backend.exceptions.IsNullOrEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Component
public class ItemControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IsNullOrEmptyException.class)
    public ResponseEntity<String> handleIsNullOrEmptyException(IsNullOrEmptyException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("all input-fields must be filled");
    }


}
