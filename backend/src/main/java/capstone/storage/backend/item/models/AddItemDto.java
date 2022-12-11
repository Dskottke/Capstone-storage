package capstone.storage.backend.item.models;
public record AddItemDto(
        String ean,
        int itemNumber,
        int storableValue) {
}
