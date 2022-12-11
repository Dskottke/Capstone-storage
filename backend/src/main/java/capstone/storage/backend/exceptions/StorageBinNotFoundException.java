package capstone.storage.backend.exceptions;

public class StorageBinNotFoundException extends RuntimeException {
    public StorageBinNotFoundException() {
        super("Can't find storage-bin.");
    }
}
