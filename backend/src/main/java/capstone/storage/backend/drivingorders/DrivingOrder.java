package capstone.storage.backend.drivingorders;

public record DrivingOrder(
        String id,
        String storageBinNumber,
        String itemNumber,
        Type type,
        String amount
) {
}
