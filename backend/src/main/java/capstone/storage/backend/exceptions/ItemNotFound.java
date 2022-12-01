package capstone.storage.backend.exceptions;

public class ItemNotFound extends RuntimeException {
    public ItemNotFound() {
        super("ean to find doesn't match with response ean");
    }
}
