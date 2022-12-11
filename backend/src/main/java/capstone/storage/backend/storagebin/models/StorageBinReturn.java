package capstone.storage.backend.storagebin.models;

public record StorageBinReturn(
        String id,
        String locationId,
        int itemNumber,
        int amount,
        String storedItemName) {

}
