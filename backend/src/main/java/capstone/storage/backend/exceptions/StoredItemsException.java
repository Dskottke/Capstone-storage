package capstone.storage.backend.exceptions;

public class StoredItemsException extends RuntimeException {
    public StoredItemsException() {
        super("can't delete because there are open orders or items stored");
    }
}

