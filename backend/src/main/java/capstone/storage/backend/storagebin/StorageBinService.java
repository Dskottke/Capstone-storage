package capstone.storage.backend.storagebin;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.item.models.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StorageBinService {
    private final StorageBinRepo repo;


    public StorageBin findStorageBinByLocationId(String storageBinNumber) {
        return repo.findItemByLocationId(storageBinNumber);
    }

    public List<StorageBin> getAllStorageBins() {
        return repo.findAll();
    }

    public boolean existsByLocationId(String locationNumber) {
        return repo.existsByLocationId(locationNumber);
    }

    public void updateInputStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdateInput = repo.findById(drivingOrder.storageLocationId())
                .orElseThrow();

        int newAmountInput = Integer.parseInt(storageBinToUpdateInput.amount()) + Integer.parseInt(drivingOrder.amount());

        StorageBin updateInput = new StorageBin(
                storageBinToUpdateInput.id(),
                drivingOrder.storageLocationId(),
                drivingOrder.itemNumber(),
                Integer.toString(newAmountInput));

        repo.save(updateInput);
    }

    public void updateOutputStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdateOutput = repo.findById(drivingOrder.storageLocationId())
                .orElseThrow();

        int newAmountOutput = Integer.parseInt(storageBinToUpdateOutput.amount()) - Integer.parseInt(drivingOrder.amount());

        StorageBin updateOutput = new StorageBin(
                storageBinToUpdateOutput.id(),
                drivingOrder.storageLocationId(),
                drivingOrder.itemNumber(),
                Integer.toString(newAmountOutput));

        repo.save(updateOutput);
    }

    public String getItemAmountFromStorageBinsByItemNumber(Item itemToCount) {
        AtomicInteger finalItemAmount = new AtomicInteger();
        List<StorageBin> storageBinsWithItem = repo.findAllByItemNumber(itemToCount.itemNumber());
        storageBinsWithItem.forEach(storageBin -> finalItemAmount.addAndGet(Integer.parseInt(storageBin.amount())));
        return Integer.toString(finalItemAmount.get());
    }
}

