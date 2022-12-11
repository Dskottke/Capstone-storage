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

    @ExceptionHandler({
            StorableValueUpdateException.class,
            ItemISNotExistingException.class,
            StoredItemsException.class,
            IsNullOrEmptyException.class,
            ItemValidationException.class,
            ItemAlreadyExistException.class,
            ItemForbiddenRequestException.class,
            ItemResponseEanNullException.class,
            IsNotEnoughSpaceException.class,
            StorageBinFalseItemException.class,
            ItemOrStorageBinNotExistingException.class,
            NotEnoughItemsRemainingException.class})
    public ResponseEntity<String> handleBadRequestResponseException(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({ItemToDeleteNotFoundException.class, OrderToDeleteNotFoundException.class})
    public ResponseEntity<String> handleNotFoundResponseException(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }


}
