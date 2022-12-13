package capstone.storage.backend.exceptions;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
        super("Can't find item");
    }
}
