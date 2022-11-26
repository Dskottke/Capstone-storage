package capstone.storage.backend.exceptions;

public class ItemForbiddenRequestException extends RuntimeException {
    public ItemForbiddenRequestException(String message) {
        super(message);
    }
}
