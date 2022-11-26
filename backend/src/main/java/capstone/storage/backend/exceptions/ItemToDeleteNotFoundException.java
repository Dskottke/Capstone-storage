package capstone.storage.backend.exceptions;

public class ItemToDeleteNotFoundException extends RuntimeException {
    public ItemToDeleteNotFoundException(String message) {
        super(message);
    }
}
