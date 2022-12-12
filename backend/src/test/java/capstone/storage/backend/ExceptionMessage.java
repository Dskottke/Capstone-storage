package capstone.storage.backend;


public enum ExceptionMessage {
    EAN_API_RESPONSE_EXCEPTION_MESSAGE("The api responseBody is null or invalid."),
    IS_NOT_ENOUGH_SPACE_EXCEPTION_MESSAGE("There is not enough space in storage-bin: "),
    IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE("All input fields must be filled!"),

    ITEM_FORBIDDEN_REQUEST_EXCEPTION_MESSAGE("forbidden request"),
    ITEM_OR_STORAGE_BIN_NOT_EXISTING_EXCEPTION_MESSAGE("Item or storage-bin doesn't exist."),

    TEST_DATA_ITEMS_NOT_FOUND_MESSAGE("can't find data from json"),
    NOT_ENOUGH_ITEMS_REMAINING_EXCEPTION_MESSAGE("Not enough items remaining."),
    ITEM_IS_NOT_EXISTING_MESSAGE("Item is not existing!"),
    ILLEGAL_TYPE_EXCEPTION_MESSAGE("The type is not allowed!"),
    API_ITEM_NOT_FOUND_EXCEPTION_MESSAGE("EAN doesn't match with response EAN from Api.");

    private final String message;


    ExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
