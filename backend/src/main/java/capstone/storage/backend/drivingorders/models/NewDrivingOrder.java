package capstone.storage.backend.drivingorders.models;

public record NewDrivingOrder(
        String storageLocationNumber,
        String itemNumber,
        String amount
) {
}
