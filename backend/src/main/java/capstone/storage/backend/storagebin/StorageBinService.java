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
    private final ItemRepo itemRepository;

    /**
     * Returns the storageBin with the given locationId from database
     *
     * @param locationId to find the StorageBin
     * @return StorageBin
     */
    public StorageBin findStorageBinByLocationId(String locationId) {
        return storageBinRepo.findStorageBinByLocationId(locationId);
    }

    /**
     * Returns a list of storageBinReturn with associated item names after calling
     * toStorageBinReturn method for each StorageBin
     *
     * @return List
     */
    public List<StorageBinReturn> getAllStorageBins() {

        return storageBinRepo.findAll()
                .stream()
                .map(this::toStorageBinReturn)
                .toList();
    }

    /**
     * This method initializes a new StorageBinReturn object for a StorageBin
     * with a storedItemName default: "" when the itemNumber is 0
     * or with the matching item name when the itemNumber is greater 0
     *
     * @param storageBin from database
     * @return StorageBinReturn
     */
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

    /**
     * This method returns the name of the item from the database.
     * could throw an ItemIsNotExistingException when the itemRepository method: findItemByItemNumber
     * doesn't find the item by itemNumber
     *
     * @param itemNumber to find the item name from database
     * @return String
     */
    private String loadItemName(int itemNumber) {
        return itemRepository.findItemByItemNumber(itemNumber).map(Item::name).orElseThrow(ItemISNotExistingException::new);

    }

    public boolean existsByLocationId(String locationId) {
        return storageBinRepo.existsByLocationId(locationId);
    }

    /**
     * This method fetches the StorageBin from the database by drivingOrder.locationId
     * and calculates the new amount of the storageBin adds with drivingOrder amount
     * and updates the drivingOrder with the new amount in database
     *
     * @param drivingOrder for the storageBin update
     */
    public void updateInputStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdateInput = storageBinRepo.findById(drivingOrder.storageLocationId())
                .orElseThrow(() -> new StorageBinNotFoundException(drivingOrder.storageLocationId()));

        int newAmountInput = storageBinToUpdateInput.amount() + drivingOrder.amount();

        StorageBin updateInput = new StorageBin(
                storageBinToUpdateInput.id(),
                drivingOrder.storageLocationId(),
                drivingOrder.itemNumber(),
                newAmountInput);

        storageBinRepo.save(updateInput);
    }

    /**
     * This method fetches the StorageBin from the database by drivingOrder.locationId
     * and calculates the new amount of the storageBin subtracting with drivingOrder amount.
     * If the storageBin is empty the storageBin will be updated with the calculated amount and an emptyStorageItemNumber 0.
     * If the storageBin is not empty the storageBin will be updated with the calculated amount.
     *
     * @param storageBinIsEmpty boolean if the storageBin is empty
     * @param drivingOrder      the drivingOrder that updates the storageBin
     */
    public void updateOutputStorageBin(boolean storageBinIsEmpty, DrivingOrder drivingOrder) {

        StorageBin storageBinToUpdateOutput = storageBinRepo.findById(drivingOrder.storageLocationId()).
                orElseThrow(() -> new StorageBinNotFoundException(drivingOrder.storageLocationId()));

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

    /**
     * This method fetches all StorageBins by itemNumber and adds all amounts to a total amount
     * which it returns.
     *
     * @return int
     */

    public int getTotalAmountFromStorageBins(Item totalizingItem) {
        AtomicInteger totalItemAmount = new AtomicInteger();
        List<StorageBin> storageBinsWithItem = storageBinRepo.findAllByItemNumber(totalizingItem.itemNumber());
        storageBinsWithItem.forEach(storageBin -> totalItemAmount.addAndGet(storageBin.amount()));
        return totalItemAmount.get();
    }

    public boolean existsByItemNumber(int itemNumber) {
        return storageBinRepo.existsByItemNumber(itemNumber);
    }
}
