package capstone.storage.backend.exceptions;

public class EanApiResponseException extends RuntimeException {
    public EanApiResponseException() {
        super("couldn't find item by ean");
    }
}
