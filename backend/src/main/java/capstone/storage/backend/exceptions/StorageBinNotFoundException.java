package capstone.storage.backend.exceptions;

public class StorageBinNotFoundException extends RuntimeException {
    public StorageBinNotFoundException(String locationId) {
        super("Can't find storage-bin with ID: " + locationId);
    }
}
