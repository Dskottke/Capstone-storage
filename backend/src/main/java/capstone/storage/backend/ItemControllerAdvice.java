package capstone.storage.backend;

import capstone.storage.backend.exceptions.IsNullOrEmptyException;
import capstone.storage.backend.exceptions.ItemAlreadyExistException;
import capstone.storage.backend.exceptions.ItemValidationException;
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ItemValidationException.class)
    public ResponseEntity<String> handleItemValidationException(ItemValidationException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ItemAlreadyExistException.class)
    public ResponseEntity<String> handleItemAlreadyExistException(ItemAlreadyExistException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }


}
