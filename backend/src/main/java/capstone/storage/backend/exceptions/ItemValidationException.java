package capstone.storage.backend.exceptions;

public class ItemValidationException extends RuntimeException {

    public ItemValidationException(int itemNumber, int storableValue) {
        super("The capacity: " + storableValue + " and the item-number: " + itemNumber + " must be greater than 0.");
    }
}
