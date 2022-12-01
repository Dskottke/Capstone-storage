package capstone.storage.backend.exceptions;

public class ItemValidationException extends RuntimeException {

    public ItemValidationException() {
        super("capacity and the item-number must be greater than 0");
    }
}
