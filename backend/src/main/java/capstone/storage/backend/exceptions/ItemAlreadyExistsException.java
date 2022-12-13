package capstone.storage.backend.exceptions;

public class ItemAlreadyExistsException extends RuntimeException {
    public ItemAlreadyExistsException(String ean) {
        super("The item with ean: " + ean + " is already existing!");
    }
}
