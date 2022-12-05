package capstone.storage.backend.exceptions;

public class StorageBinFalseItemException extends RuntimeException {
    public StorageBinFalseItemException() {
        super("storage-bin is filled with not matching item");
    }
}
