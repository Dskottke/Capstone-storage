package capstone.storage.backend.exceptions;

public class ItemAlreadyExistException extends RuntimeException {
    public ItemAlreadyExistException() {
        super("item is already existing");
    }
}
