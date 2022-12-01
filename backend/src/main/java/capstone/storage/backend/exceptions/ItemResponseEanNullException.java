package capstone.storage.backend.exceptions;

public class ItemResponseEanNullException extends RuntimeException {
    public ItemResponseEanNullException() {
        super("couldn't find item by ean");
    }
}
