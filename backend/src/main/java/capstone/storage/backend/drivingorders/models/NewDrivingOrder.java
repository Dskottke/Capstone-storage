package capstone.storage.backend.drivingorders.models;

public record NewDrivingOrder(
        String storageBinNumber,
        String itemNumber,
        String amount
) {
}
