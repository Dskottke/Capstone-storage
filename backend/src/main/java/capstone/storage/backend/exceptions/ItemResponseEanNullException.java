package capstone.storage.backend.exceptions;

public class ItemResponseEanNullException extends RuntimeException {
    public ItemResponseEanNullException(String ean) {
        super("couldn't find item by ean: " + ean);
    }
}
