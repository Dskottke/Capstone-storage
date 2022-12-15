package capstone.storage.backend;

import capstone.storage.backend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Component
public class BackendExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            StorableValueUpdateException.class,
            IllegalTypeException.class,
            ItemISNotExistingException.class,
            StoredItemsException.class,
            IsNullOrEmptyException.class,
            ItemValidationException.class,
            ItemAlreadyExistsException.class,
            ItemResponseEanNullException.class,
            IsNotEnoughSpaceException.class,
            StorageBinFalseItemException.class,
            ItemOrStorageBinNotExistingException.class,
            NotEnoughItemsRemainingException.class})
    public ResponseEntity<String> handleBadRequestResponseException(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({
            ItemNotFoundException.class,
            ItemToDeleteNotFoundException.class,
            OrderToDeleteNotFoundException.class,
            StorageBinNotFoundException.class,})
    public ResponseEntity<String> handleNotFoundResponseException(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }


}
