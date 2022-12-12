package capstone.storage.backend.exceptions;

public class OrderToDeleteNotFoundException extends RuntimeException {
    public OrderToDeleteNotFoundException(String id) {
        super("Couldn't delete order with ID: " + id + " because it doesn't exist");
    }
}
