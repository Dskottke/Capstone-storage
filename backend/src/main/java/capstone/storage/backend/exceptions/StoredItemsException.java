package capstone.storage.backend.exceptions;

public class StoredItemsException extends RuntimeException {
    public StoredItemsException(String id) {
        super("Can't delete item with ID: " + id + " because there are open driving-orders or items are still stored");
    }
}

