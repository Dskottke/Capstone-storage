package capstone.storage.backend.exceptions;

public class ItemOrStorageBinNotExistingException extends RuntimeException {
    public ItemOrStorageBinNotExistingException() {
        super("Item or storage-bin doesn't exist.");
    }
}
