package capstone.storage.backend.storagebin;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StorageBinService {
    private final StorageBinRepo storageBinRepo;

    private final ItemRepo itemRepo;

    public StorageBin findStorageBinByLocationId(String storageBinNumber) {
        return storageBinRepo.findItemByLocationId(storageBinNumber);
    }

    public List<StorageBin> getAllStorageBins() {

        List<StorageBin> allStorageBins = storageBinRepo.findAll();

        return addItemNameToStorageList(allStorageBins);
    }

    public List<StorageBin> addItemNameToStorageList(List<StorageBin> allStorageBins) {

        List<StorageBin> storageBinsToReturn = new ArrayList<>();

        String noItemNumber = "0";
        for (StorageBin storageBin : allStorageBins) {
            if (!noItemNumber.equals(storageBin.itemNumber())) {

                Optional<Item> item = itemRepo.findItemByItemNumber(storageBin.itemNumber());

                item.ifPresent(value -> storageBinsToReturn.add(
                        new StorageBin(
                                storageBin.id(),
                                storageBin.locationId(),
                                storageBin.itemNumber(),
                                storageBin.amount(),
                                value.name())));

            } else {
                storageBinsToReturn.add(storageBin);
            }
        }
        return storageBinsToReturn;
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

    public void updateOutputStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdateOutput = storageBinRepo.findById(drivingOrder.storageLocationId())
                .orElseThrow();

        int newAmountOutput = Integer.parseInt(storageBinToUpdateOutput.amount()) - Integer.parseInt(drivingOrder.amount());

        StorageBin updateOutput = new StorageBin(
                storageBinToUpdateOutput.id(),
                drivingOrder.storageLocationId(),
                drivingOrder.itemNumber(),
                Integer.toString(newAmountOutput),
                storageBinToUpdateOutput.storedItemName());

        storageBinRepo.save(updateOutput);
    }

    public String getItemAmountFromStorageBinsByItemNumber(Item itemToCount) {
        AtomicInteger finalItemAmount = new AtomicInteger();
        List<StorageBin> storageBinsWithItem = storageBinRepo.findAllByItemNumber(itemToCount.itemNumber());
        storageBinsWithItem.forEach(storageBin -> finalItemAmount.addAndGet(Integer.parseInt(storageBin.amount())));
        return Integer.toString(finalItemAmount.get());
    }
}

