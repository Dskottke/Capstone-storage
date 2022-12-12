package capstone.storage.backend.exceptions;

public class ItemAlreadyExistException extends RuntimeException {
    public ItemAlreadyExistException(String ean) {
        super("The item with ean: " + ean + " is already existing!");
    }
}
