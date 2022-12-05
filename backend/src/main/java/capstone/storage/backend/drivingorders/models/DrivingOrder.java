package capstone.storage.backend.drivingorders.models;

import capstone.storage.backend.drivingorders.Type;

public record DrivingOrder(
        String id,
        String storageBinId,
        String itemNumber,
        Type type,
        String amount
) {
}
