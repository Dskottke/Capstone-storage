package capstone.storage.backend.exceptions;

public class ApiItemNotFoundException extends RuntimeException {
    public ApiItemNotFoundException() {
        super("ean to find doesn't match with response ean");
    }
}
