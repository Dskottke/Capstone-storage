package capstone.storage.backend.exceptions;

public class IsNullOrEmptyException extends RuntimeException {
    public IsNullOrEmptyException() {
        super("All input fields must be filled!");
    }
}
