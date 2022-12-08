package capstone.storage.backend.storagebin;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StorageBinService {
    private final StorageBinRepo storageBinRepo;
    private final ItemRepo itemRepo;

    public StorageBin findStorageBinByLocationId(String storageBinNumber) {
        return storageBinRepo.findStorageBinByLocationId(storageBinNumber);
    }

    public List<StorageBin> getAllStorageBins() {
        addItemNameToStorageList();
        return storageBinRepo.findAll();
    }

    public void addItemNameToStorageList() {

        List<StorageBin> storageBinList = storageBinRepo.findAll();

        for (StorageBin storageBin : storageBinList) {
            if (Integer.parseInt(storageBin.itemNumber()) > 0) {

                Optional<Item> item = itemRepo.findItemByItemNumber(storageBin.itemNumber());

                item.ifPresent(update -> storageBinRepo.save(
                        new StorageBin(
                                storageBin.id(),
                                storageBin.locationId(),
                                storageBin.itemNumber(),
                                storageBin.amount(),
                                update.name())));
            }
        }
    }

    public boolean existsByLocationId(String locationNumber) {
        return storageBinRepo.existsByLocationId(locationNumber);
    }

    public void updateInputStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdateInput = storageBinRepo.findById(drivingOrder.storageLocationId())
                .orElseThrow();

        int newAmountInput = Integer.parseInt(storageBinToUpdateInput.amount()) + Integer.parseInt(drivingOrder.amount());

        StorageBin updateInput = new StorageBin(
                storageBinToUpdateInput.id(),
                drivingOrder.storageLocationId(),
                drivingOrder.itemNumber(),
                Integer.toString(newAmountInput),
                storageBinToUpdateInput.storedItemName());

        storageBinRepo.save(updateInput);
    }

    public void updateOutputStorageBin(boolean storageBinIsEmpty, DrivingOrder drivingOrder) {

        StorageBin storageBinToUpdateOutput = storageBinRepo.findById(drivingOrder.storageLocationId()).orElseThrow();

        int newAmountOutput = Integer.parseInt(storageBinToUpdateOutput.amount()) - Integer.parseInt(drivingOrder.amount());

        if (storageBinIsEmpty) {
            String emptyStorageItemName = "";
            String emptyStorageItemNumber = "0";
            StorageBin updateOutput = new StorageBin(
                    storageBinToUpdateOutput.id(),
                    drivingOrder.storageLocationId(),
                    emptyStorageItemNumber,
                    Integer.toString(newAmountOutput),
                    emptyStorageItemName);
            storageBinRepo.save(updateOutput);
        } else {
            StorageBin updateOutput = new StorageBin(
                    storageBinToUpdateOutput.id(),
                    drivingOrder.storageLocationId(),
                    drivingOrder.itemNumber(),
                    Integer.toString(newAmountOutput),
                    storageBinToUpdateOutput.storedItemName());
            storageBinRepo.save(updateOutput);
        }
    }

    public String getItemAmountFromStorageBinsByItemNumber(Item itemToCount) {
        AtomicInteger finalItemAmount = new AtomicInteger();
        List<StorageBin> storageBinsWithItem = storageBinRepo.findAllByItemNumber(itemToCount.itemNumber());
        storageBinsWithItem.forEach(storageBin -> finalItemAmount.addAndGet(Integer.parseInt(storageBin.amount())));
        return Integer.toString(finalItemAmount.get());
    }
}
