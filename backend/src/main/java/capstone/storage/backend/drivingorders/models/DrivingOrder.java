package capstone.storage.backend.drivingorders.models;

import capstone.storage.backend.drivingorders.Type;

public record DrivingOrder(
        String id,
        String storageLocationId,
        String itemNumber,
        Type type,
        String amount
) {
}
