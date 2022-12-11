package capstone.storage.backend.storagebin;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.models.StorageBin;
import capstone.storage.backend.storagebin.models.StorageBinReturn;
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

    public StorageBin findStorageBinByLocationId(String locationId) {
        return storageBinRepo.findStorageBinByLocationId(locationId);
    }

    public List<StorageBinReturn> getAllStorageBins() {
        return addItemNameToStorageList();
    }

    public List<StorageBinReturn> addItemNameToStorageList() {

        List<StorageBin> storageBinList = storageBinRepo.findAll();
        List<StorageBinReturn> storageBinReturnList = new ArrayList<>();

        for (StorageBin storageBin : storageBinList) {
            if (Integer.parseInt(storageBin.itemNumber()) > 0) {

                Optional<Item> item = itemRepo.findItemByItemNumber(storageBin.itemNumber());

                item.ifPresent(update -> storageBinReturnList.add(
                        new StorageBinReturn(
                                storageBin.id(),
                                storageBin.locationId(),
                                storageBin.itemNumber(),
                                storageBin.amount(),
                                item.get().name())));
            } else {
                storageBinReturnList.add(
                        new StorageBinReturn(
                                storageBin.id(),
                                storageBin.locationId(),
                                storageBin.itemNumber(),
                                storageBin.amount(),
                                ""));
            }
        }
        return storageBinReturnList;
    }

    public boolean existsByLocationId(String locationId) {
        return storageBinRepo.existsByLocationId(locationId);
    }

    public void updateInputStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdateInput = storageBinRepo.findById(drivingOrder.storageLocationId())
                .orElseThrow();

        int newAmountInput = Integer.parseInt(storageBinToUpdateInput.amount()) + Integer.parseInt(drivingOrder.amount());

        StorageBin updateInput = new StorageBin(
                storageBinToUpdateInput.id(),
                drivingOrder.storageLocationId(),
                drivingOrder.itemNumber(),
                Integer.toString(newAmountInput));

        storageBinRepo.save(updateInput);
    }

    public void updateOutputStorageBin(boolean storageBinIsEmpty, DrivingOrder drivingOrder) {

        StorageBin storageBinToUpdateOutput = storageBinRepo.findById(drivingOrder.storageLocationId()).orElseThrow();

        int newAmountOutput = Integer.parseInt(storageBinToUpdateOutput.amount()) - Integer.parseInt(drivingOrder.amount());

        if (storageBinIsEmpty) {
            String emptyStorageItemNumber = "0";
            StorageBin updateOutput = new StorageBin(
                    storageBinToUpdateOutput.id(),
                    drivingOrder.storageLocationId(),
                    emptyStorageItemNumber,
                    Integer.toString(newAmountOutput));
            storageBinRepo.save(updateOutput);
        } else {
            StorageBin updateOutput = new StorageBin(
                    storageBinToUpdateOutput.id(),
                    drivingOrder.storageLocationId(),
                    drivingOrder.itemNumber(),
                    Integer.toString(newAmountOutput));
            storageBinRepo.save(updateOutput);
        }
    }

    public String getItemAmountFromStorageBinsByItemNumber(Item itemToCount) {
        AtomicInteger finalItemAmount = new AtomicInteger();
        List<StorageBin> storageBinsWithItem = storageBinRepo.findAllByItemNumber(itemToCount.itemNumber());
        storageBinsWithItem.forEach(storageBin -> finalItemAmount.addAndGet(Integer.parseInt(storageBin.amount())));
        return Integer.toString(finalItemAmount.get());
    }

    public boolean existsByItemNumber(String itemNumber) {
        return storageBinRepo.existsByItemNumber(itemNumber);
    }
}
