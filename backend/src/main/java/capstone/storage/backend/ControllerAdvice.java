package capstone.storage.backend;

import capstone.storage.backend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@org.springframework.web.bind.annotation.ControllerAdvice
@Component
public class ControllerAdvice extends ResponseEntityExceptionHandler {


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

    @ExceptionHandler(ItemForbiddenRequestException.class)
    public ResponseEntity<String> handleItemForbiddenRequestException(ItemForbiddenRequestException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ItemToDeleteNotFoundException.class)
    public ResponseEntity<String> handleItemToDeleteNotFoundException(ItemToDeleteNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ItemResponseEanNullException.class)
    public ResponseEntity<String> handleItemResponseEanNullException(ItemResponseEanNullException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IsNotEnoughSpaceException.class)
    public ResponseEntity<String> handleIsNotEnoughSpaceException(IsNotEnoughSpaceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(StorageBinFalseItemException.class)
    public ResponseEntity<String> handleStorageBinFalseItemException(StorageBinFalseItemException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(OrderToDeleteNotFoundException.class)
    public ResponseEntity<String> handleStorageBinFalseItemException(OrderToDeleteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ItemOrStorageBinNotExistingException.class)
    public ResponseEntity<String> handleItemOrStorageBinNotExistingException(ItemOrStorageBinNotExistingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotEnoughItemsRemainingException.class)
    public ResponseEntity<String> handleNotEnoughItemsRemainingException(NotEnoughItemsRemainingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}