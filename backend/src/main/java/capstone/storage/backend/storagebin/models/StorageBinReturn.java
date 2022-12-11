package capstone.storage.backend.storagebin.models;

public record StorageBinReturn(
        String id,
        String locationId,
        String itemNumber,
        String amount,
        String storedItemName) {

}
