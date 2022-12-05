package capstone.storage.backend.exceptions;

public class ItemOrStorageBinNotExistingException extends RuntimeException {
    public ItemOrStorageBinNotExistingException() {
        super("item or storage-bin doesn't exist");
    }
}
