package capstone.storage.backend.exceptions;

public class ItemISNotExistingException extends RuntimeException {
    public ItemISNotExistingException() {
        super("Item is not existing!");
    }
}
