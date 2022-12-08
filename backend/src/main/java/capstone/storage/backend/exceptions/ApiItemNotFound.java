package capstone.storage.backend.exceptions;

public class ApiItemNotFound extends RuntimeException {
    public ApiItemNotFound() {
        super("ean to find doesn't match with response ean");
    }
}
