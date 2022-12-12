package capstone.storage.backend.exceptions;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String id) {
        super("Can't find item by ID: " + id);
    }
}
