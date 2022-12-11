package capstone.storage.backend.storagebin.models;

public record StorageBin(
        String id,
        String locationId,
        String itemNumber,
        String amount
) {
}
