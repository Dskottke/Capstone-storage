package capstone.storage.backend.exceptions;

public class ItemNotExistingException extends RuntimeException {
    public ItemNotExistingException() {
        super("item is not existing");
    }
}
