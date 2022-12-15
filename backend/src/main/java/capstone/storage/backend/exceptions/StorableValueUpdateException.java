package capstone.storage.backend.exceptions;

public class StorableValueUpdateException extends RuntimeException {
    public StorableValueUpdateException() {
        super("can't change the capacity to less than stored");
    }
}
