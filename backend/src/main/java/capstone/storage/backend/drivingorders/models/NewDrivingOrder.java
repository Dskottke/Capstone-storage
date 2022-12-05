package capstone.storage.backend.drivingorders.models;

public record NewDrivingOrder(
        String storageLocationId,
        String itemNumber,
        String amount
) {
}
