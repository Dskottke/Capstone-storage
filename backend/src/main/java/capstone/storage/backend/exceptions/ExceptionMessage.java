package capstone.storage.backend.exceptions;

public enum ExceptionMessage {

    IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE("all input-fields must be filled"),
    ITEM_VALIDATION_EXCEPTION_MESSAGE("capacity and the item-number must be greater than 0"),
    ITEM_ALREADY_EXIST_EXCEPTION_MESSAGE("item is already existing"),
    ITEM_TO_DELETE_NOT_FOUND_EXCEPTION_MESSAGE("item is already deleted"),
    ITEM_FORBIDDEN_REQUEST_EXCEPTION_MESSAGE("forbidden request"),
    ITEM_RESPONSE_EAN_NULL_EXCEPTION_MESSAGE("couldn't find item by ean"),
    EAN_API_RESPONSE_EXCEPTION_MESSAGE("couldn't find item by ean"),
    ITEM_NOT_FOUND_EXCEPTION_MESSAGE("ean to find doesn't match with response ean");


    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
