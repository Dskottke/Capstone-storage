package capstone.storage.backend.exceptions;

public class IsNullOrEmptyException extends RuntimeException {
    public IsNullOrEmptyException() {
        super("all input-fields must be filled");
    }
}
