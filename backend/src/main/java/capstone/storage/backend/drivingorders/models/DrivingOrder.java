package capstone.storage.backend.drivingorders.models;

import capstone.storage.backend.drivingorders.Type;

public record DrivingOrder(
        String id,
        String storageLocationId,
        int itemNumber,
        Type type,
        int amount
) {
}
