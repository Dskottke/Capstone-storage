package capstone.storage.backend.exceptions;

public class ItemToDeleteNotFoundException extends RuntimeException {
    public ItemToDeleteNotFoundException(String id) {
        super("Couldn't delete item with ID: " + id + " because it doesn't exist.");
    }
}
