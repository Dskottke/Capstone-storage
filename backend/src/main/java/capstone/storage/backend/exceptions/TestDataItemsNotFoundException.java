package capstone.storage.backend.exceptions;

public class TestDataItemsNotFoundException extends RuntimeException {
    public TestDataItemsNotFoundException() {
        super("can't find data from json");
    }
}
