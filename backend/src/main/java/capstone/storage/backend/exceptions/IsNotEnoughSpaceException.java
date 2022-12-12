package capstone.storage.backend.exceptions;

public class IsNotEnoughSpaceException extends RuntimeException {
    public IsNotEnoughSpaceException(String storageBinLocationId) {
        super("There is not enough space in storage-bin: " + storageBinLocationId);
    }
}
