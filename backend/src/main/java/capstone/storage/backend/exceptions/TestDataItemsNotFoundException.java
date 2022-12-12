package capstone.storage.backend.exceptions;

public class TestDataItemsNotFoundException extends RuntimeException {
    public TestDataItemsNotFoundException() {
        super("Can't read data from json");
    }
}
