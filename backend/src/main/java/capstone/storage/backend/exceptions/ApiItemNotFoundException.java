package capstone.storage.backend.exceptions;

public class ApiItemNotFoundException extends RuntimeException {
    public ApiItemNotFoundException() {
        super("EAN doesn't match with response EAN from Api.");
    }
}
