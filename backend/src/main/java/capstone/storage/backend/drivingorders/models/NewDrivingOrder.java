package capstone.storage.backend.drivingorders.models;

public record NewDrivingOrder(
        String storageLocationId,
        int itemNumber,
        int amount
) {
}
