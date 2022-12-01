package capstone.storage.backend.exceptions;

public class ItemForbiddenRequestException extends RuntimeException {
    public ItemForbiddenRequestException() {
        super("forbidden request");
    }
}
