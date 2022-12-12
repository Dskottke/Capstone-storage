package capstone.storage.backend.exceptions;

public class EanApiResponseException extends RuntimeException {
    public EanApiResponseException() {
        super("The api responseBody is null or invalid.");
    }
}
