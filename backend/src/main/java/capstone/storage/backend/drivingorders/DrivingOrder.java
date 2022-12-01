package capstone.storage.backend.drivingorders;


import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.StorageBin;

public record DrivingOrder(
        String id,
        StorageBin storageBin,
        Item item,
        DrivingOrderType drivingOrderType,
        String amount
) {
}
