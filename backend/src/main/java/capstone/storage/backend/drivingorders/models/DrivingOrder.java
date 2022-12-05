package capstone.storage.backend.drivingorders.models;

import capstone.storage.backend.drivingorders.Type;

public record DrivingOrder(
        String id,
        String storageBinNumber,
        String itemNumber,
        Type type,
        String amount
) {
}
