package capstone.storage.backend.exceptions;

public class IsNullOrEmptyException extends RuntimeException {
    public IsNullOrEmptyException(String message) {
        super(message);
    }
}
