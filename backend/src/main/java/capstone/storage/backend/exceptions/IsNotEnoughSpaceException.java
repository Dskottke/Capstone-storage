package capstone.storage.backend.exceptions;

public class IsNotEnoughSpaceException extends RuntimeException {
    public IsNotEnoughSpaceException() {
        super("not enough space in storage-bin");
    }
}
