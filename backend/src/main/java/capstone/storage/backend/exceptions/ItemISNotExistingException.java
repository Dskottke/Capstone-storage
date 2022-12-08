package capstone.storage.backend.exceptions;

public class ItemISNotExistingException extends RuntimeException {
    public ItemISNotExistingException() {
        super("item is not existing");
    }
}
