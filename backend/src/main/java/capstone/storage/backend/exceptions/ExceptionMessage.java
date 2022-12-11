package capstone.storage.backend.exceptions;

public enum ExceptionMessage {
    EAN_API_RESPONSE_EXCEPTION_MESSAGE("couldn't find item by ean"),
    IS_NOT_ENOUGH_SPACE_EXCEPTION_MESSAGE("not enough space in storage-bin"),
    IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE("all input-fields must be filled"),
    ITEM_ALREADY_EXIST_EXCEPTION_MESSAGE("item is already existing"),
    ITEM_FORBIDDEN_REQUEST_EXCEPTION_MESSAGE("forbidden request"),
    ITEM_NOT_FOUND_EXCEPTION_MESSAGE("ean to find doesn't match with response ean"),
    ITEM_RESPONSE_EAN_NULL_EXCEPTION_MESSAGE("couldn't find item by ean"),
    ITEM_TO_DELETE_NOT_FOUND_EXCEPTION_MESSAGE("item is already deleted"),
    ITEM_VALIDATION_EXCEPTION_MESSAGE("capacity and the item-number must be greater than 0"),
    ITEM_OR_STORAGE_BIN_NOT_EXISTING_EXCEPTION_MESSAGE("item or storage-bin doesn't exist"),
    ORDER_TO_DELETE_NOT_FOUND_EXCEPTION_MESSAGE("order to delete not found"),
    STORAGE_BIN_FALSE_ITEM_EXCEPTION_MESSAGE("storage-bin is filled with not matching item"),
    TEST_DATA_ITEMS_NOT_FOUND_MESSAGE("can't find data from json"),
    NOT_ENOUGH_ITEMS_REMAINING_EXCEPTION_MESSAGE("not enough items remaining"),

    STORED_ITEMS_EXCEPTION("can't delete because there are open orders or items stored"),

    ITEM_IS_NOT_EXISTING("item is not existing"),
    STORABLE_VALUE_UPDATE_EXCEPTION_MESSAGE("can't change the capacity to less than stored");
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
