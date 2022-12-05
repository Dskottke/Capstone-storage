package capstone.storage.backend.exceptions;

public class OrderToDeleteNotFoundException extends RuntimeException {
    public OrderToDeleteNotFoundException() {
        super("order to delete not found");
    }
}
