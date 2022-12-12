package capstone.storage.backend.exceptions;

public class StorageBinFalseItemException extends RuntimeException {
    public StorageBinFalseItemException(int storageBinItemNumber, int newOrderItemNumber) {
        super("Storage-bin item-nr.: " + storageBinItemNumber + " and order item-nr.: " + newOrderItemNumber + " doesn't match");
    }
}
