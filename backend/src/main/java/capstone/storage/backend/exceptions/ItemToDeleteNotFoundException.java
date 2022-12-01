package capstone.storage.backend.exceptions;

public class ItemToDeleteNotFoundException extends RuntimeException {
    public ItemToDeleteNotFoundException() {
        super("item is already deleted");
    }
}
