package capstone.storage.backend.storagebin;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.exceptions.ItemISNotExistingException;
import capstone.storage.backend.exceptions.StorageBinNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.models.StorageBin;
import capstone.storage.backend.storagebin.models.StorageBinReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

        return storageBinRepo.findAll()
                .stream()
                .map(this::toStorageBinReturn)
                .toList();
    }

    private StorageBinReturn toStorageBinReturn(StorageBin storageBin) {
        int itemNumber = storageBin.itemNumber();
        String storedItemName = itemNumber > 0 ? loadItemName(itemNumber) : "";
        return new StorageBinReturn(
                storageBin.id(),
                storageBin.locationId(),
                itemNumber,
                storageBin.amount(),
                storedItemName);
    }

    private String loadItemName(int itemNumber) {
        return itemRepo.findItemByItemNumber(itemNumber).map(Item::name).orElseThrow(ItemISNotExistingException::new);

    }

    public boolean existsByLocationId(String locationId) {
        return storageBinRepo.existsByLocationId(locationId);
    }

    public void updateInputStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdateInput = storageBinRepo.findById(drivingOrder.storageLocationId()).orElseThrow(StorageBinNotFoundException::new);

        int newAmountInput = storageBinToUpdateInput.amount() + drivingOrder.amount();

        StorageBin updateInput = new StorageBin(
                storageBinToUpdateInput.id(),
                drivingOrder.storageLocationId(),
                drivingOrder.itemNumber(),
                newAmountInput);

        storageBinRepo.save(updateInput);
    }

    public void updateOutputStorageBin(boolean storageBinIsEmpty, DrivingOrder drivingOrder) {

        StorageBin storageBinToUpdateOutput = storageBinRepo.findById(drivingOrder.storageLocationId()).orElseThrow();

        int newAmountOutput = storageBinToUpdateOutput.amount() - drivingOrder.amount();

        if (storageBinIsEmpty) {
            int emptyStorageItemNumber = 0;
            StorageBin updateOutput = new StorageBin(
                    storageBinToUpdateOutput.id(),
                    drivingOrder.storageLocationId(),
                    emptyStorageItemNumber,
                    newAmountOutput);
            storageBinRepo.save(updateOutput);
        } else {
            StorageBin updateOutput = new StorageBin(
                    storageBinToUpdateOutput.id(),
                    drivingOrder.storageLocationId(),
                    drivingOrder.itemNumber(),
                    newAmountOutput);
            storageBinRepo.save(updateOutput);
        }
    }

    public int getItemAmountFromStorageBinsByItemNumber(Item itemToCount) {
        AtomicInteger finalItemAmount = new AtomicInteger();
        List<StorageBin> storageBinsWithItem = storageBinRepo.findAllByItemNumber(itemToCount.itemNumber());
        storageBinsWithItem.forEach(storageBin -> finalItemAmount.addAndGet(storageBin.amount()));
        return finalItemAmount.get();
    }

    public boolean existsByItemNumber(int itemNumber) {
        return storageBinRepo.existsByItemNumber(itemNumber);
    }
}
