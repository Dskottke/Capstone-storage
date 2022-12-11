package capstone.storage.backend.exceptions;


public class IllegalTypeException extends RuntimeException {
    public IllegalTypeException() {
        super("The type is not allowed!");
    }
}
