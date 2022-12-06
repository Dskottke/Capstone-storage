package capstone.storage.backend.exceptions;

public class NotEnoughItemsRemainingException extends RuntimeException {
    public NotEnoughItemsRemainingException() {
        super("not enough items remaining");
    }
}
