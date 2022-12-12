package capstone.storage.backend.exceptions;

public class NotEnoughItemsRemainingException extends RuntimeException {
    public NotEnoughItemsRemainingException(String locationId) {
        super("Not enough items remaining on storageBin with ID: " + locationId);
    }
}
