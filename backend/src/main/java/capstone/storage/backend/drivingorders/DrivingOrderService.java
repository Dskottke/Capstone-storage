package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.exceptions.*;
import capstone.storage.backend.item.ItemService;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.StorageBinService;
import capstone.storage.backend.storagebin.models.StorageBin;
import capstone.storage.backend.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


@RequiredArgsConstructor
@Service
public class DrivingOrderService {
    private final DrivingOrderRepo drivingOrderRepo;
    private final ItemService itemService;
    private final ServiceUtils serviceUtils;
    private final StorageBinService storageBinService;

    public List<DrivingOrder> getAllDrivingOrdersByType(Type type) {
        return drivingOrderRepo.findByType(type);
    }

    /**
     * The method ensures that the newDrivingOrder is validated by the validateNewDrivingOrder method.
     * The method inserts a new DrivingOrder with the fields of the newDrivingOrder into the database
     *
     * @param type            type of the DrivingOrder
     * @param newDrivingOrder newDrivingOrder to add
     * @return DrivingOrder
     */
    public DrivingOrder addNewDrivingOrder(Type type, NewDrivingOrder newDrivingOrder) {
        validateNewDrivingOrder(type, newDrivingOrder);
        DrivingOrder drivingOrder = new DrivingOrder(
                serviceUtils.generateUUID(),
                newDrivingOrder.storageLocationId(),
                newDrivingOrder.itemNumber(), type,
                newDrivingOrder.amount());
        return drivingOrderRepo.insert(drivingOrder);
    }

    /**
     * This method handles the different validation methods for the DrivingOrder, depending on its type.<Br>.
     * It also takes care -> regardless of type -> of being checked by the itemAndStorageBinExisting method.
     *
     * @param type            the type of the NewDrivingOrder
     * @param newDrivingOrder newDriving to validate
     */
    private void validateNewDrivingOrder(Type type, NewDrivingOrder newDrivingOrder) {
        if (!itemAndStorageBinExisting(newDrivingOrder)) {
            throw new ItemOrStorageBinNotExistingException();
        }
        Item itemFromOrder = itemService.findItemByItemNumber(newDrivingOrder.itemNumber());
        StorageBin storageBinFromOrder = storageBinService.findStorageBinByLocationId(newDrivingOrder.storageLocationId());

        if (type == Type.INPUT) {
            validateInputOrder(storageBinFromOrder, itemFromOrder, newDrivingOrder);

        } else if (type == Type.OUTPUT) {
            validateOutputOrder(storageBinFromOrder, itemFromOrder, newDrivingOrder);
        } else {
            throw new IllegalTypeException();
        }
    }

    /**
     * Throws StorageBinFalseItemException when the isValidOutputItem method returns false.<br>
     * Throws NotEnoughItemsRemainingException when the hasEnoughAmount method returns false.
     *
     * @param storageBinFromOrder is used for the isValidInputItem and hasFreeAmount methods
     * @param itemFromOrder       is used for the isValidInputItem and hasFreeAmount methods
     * @param newDrivingOrder     is used for the isValidInputItem and hasFreeAmount methods
     */

    private void validateOutputOrder(StorageBin storageBinFromOrder, Item itemFromOrder, NewDrivingOrder newDrivingOrder) {
        if (!isValidOutputItem(storageBinFromOrder, itemFromOrder)) {
            throw new StorageBinFalseItemException(storageBinFromOrder.itemNumber(), newDrivingOrder.itemNumber());
        }
        if (!hasEnoughAmount(storageBinFromOrder, newDrivingOrder)) {
            throw new NotEnoughItemsRemainingException(storageBinFromOrder.locationId());
        }
    }

    /**
     * Throws StorageBinFalseItemException when the isValidInputItem method returns false.<br>
     * Throws IsNotEnoughSpaceException when the hasFreeAmount method returns false.
     *
     * @param storageBinFromOrder is used for the isValidInputItem and hasFreeAmount methods
     * @param itemFromOrder       is used for the isValidInputItem and hasFreeAmount methods
     * @param newDrivingOrder     is used for the isValidInputItem and hasFreeAmount methods
     */
    private void validateInputOrder(StorageBin storageBinFromOrder, Item itemFromOrder, NewDrivingOrder newDrivingOrder) {
        if (!isValidInputItem(storageBinFromOrder, itemFromOrder.itemNumber())) {
            throw new StorageBinFalseItemException(storageBinFromOrder.itemNumber(), newDrivingOrder.itemNumber());
        }
        if (!hasFreeAmount(storageBinFromOrder, newDrivingOrder, itemFromOrder)) {
            throw new IsNotEnoughSpaceException(storageBinFromOrder.locationId());
        }
    }

    /**
     * Returns true if the newDrivingOrder fields are null or 0.<br>
     * Returns true if the newDrivingOrder field storageLocationId is an empty String.
     *
     * @param newDrivingOrder contains all the fields for the conditions
     * @return boolean
     */
    public boolean isNullOrEmpty(NewDrivingOrder newDrivingOrder) {
        if (newDrivingOrder.storageLocationId() == null || newDrivingOrder.itemNumber() == 0 || newDrivingOrder.amount() == 0) {
            return true;
        }
        String emptyString = "";
        return emptyString.equals(newDrivingOrder.storageLocationId());
    }

    /**
     * checks if the item and storageBin of the newDrivingOrder exist in the database.
     *
     * @param newDrivingOrder field itemNumber is used for methods
     * @return boolean
     */

    private boolean itemAndStorageBinExisting(NewDrivingOrder newDrivingOrder) {
        return itemService.existByItemNumber(newDrivingOrder.itemNumber()) && storageBinService.existsByLocationId(newDrivingOrder.storageLocationId());
    }

    /**
     * checks if the itemNumber of storageBin matches the itemNumber of the item.
     * returns true if the itemNumbers are the same
     * returns false if the itemNumbers are different
     *
     * @param storageBinToCheck the storageBin to validate
     * @param itemToCheck       the item to validate
     * @return boolean
     */
    public boolean isValidOutputItem(StorageBin storageBinToCheck, Item itemToCheck) {
        return storageBinToCheck.itemNumber() == itemToCheck.itemNumber();
    }

    /**
     * returns true if the StorageBinToCheck itemNumber is equal to the specified itemNumber or 0
     * and if the boolean method hasConflictingOrder returns false
     *
     * @param storageBinToCheck is the storageBin to prove
     * @param itemNumber        is the ItemNumber to prove
     * @return boolean
     */
    public boolean isValidInputItem(StorageBin storageBinToCheck, int itemNumber) {
        boolean binIsNotOccupiedByOtherItem = storageBinToCheck.itemNumber() == itemNumber || storageBinToCheck.itemNumber() == 0;
        return binIsNotOccupiedByOtherItem && !hasConflictingOrder(storageBinToCheck, itemNumber);
    }

    /**
     * returns true if there is an existing InputDrivingOrder without matching itemNumber on the StorageBin.<br>
     * returns false if there is an existing InputDrivingOrder with matching itemNumber on the StorageBin..
     *
     * @param storageBin locationId field is used to find an order with the storageBin
     * @param itemNumber the itemNumber to prove if it is equal with the storageBin - itemNumber
     * @return boolean
     */
    private boolean hasConflictingOrder(StorageBin storageBin, int itemNumber) {
        return drivingOrderRepo.findFirstByStorageLocationIdAndType(storageBin.locationId(), Type.INPUT)
                .map(drivingOrder -> drivingOrder.itemNumber() != itemNumber)
                .orElse(false);
    }

    /**
     * This method adds all drivingOrder amounts within the specified list and returns the sum.
     *
     * @param existingInputDrivingOrders is a list of DrivingOrders with Type INPUT
     * @return int
     */
    public int getTotalAmountFromList(List<DrivingOrder> existingInputDrivingOrders) {
        AtomicInteger orderTotalAmount = new AtomicInteger();
        existingInputDrivingOrders.forEach(drivingOrder -> orderTotalAmount.addAndGet(drivingOrder.amount()));
        return orderTotalAmount.get();
    }

    /**
     * Returns true if there is enough free amount on the storageBin
     * depending on the already existing INPUT drivingOrders, the already stored articles and
     * the storableValue of the item
     *
     * @param storageBinToCheck contains the already stored amount for the calculation
     * @param newDrivingOrder   contains the amount to add for the calculation
     * @param itemToCheck       contains the storableValue for the calculation
     * @return boolean
     */
    public boolean hasFreeAmount(StorageBin storageBinToCheck, NewDrivingOrder newDrivingOrder, Item itemToCheck) {

        int actualStorageBinAmount = storageBinToCheck.amount();

        int itemsToStoreAmount = newDrivingOrder.amount();

        int storageBinCapacity = itemToCheck.storableValue();

        List<DrivingOrder> existingInputDrivingOrders = drivingOrderRepo.findByTypeAndStorageLocationId(Type.INPUT, storageBinToCheck.locationId());

        int ordersTotalAmount = getTotalAmountFromList(existingInputDrivingOrders);

        int freeAmount = storageBinCapacity - (ordersTotalAmount + actualStorageBinAmount);

        return itemsToStoreAmount <= freeAmount;
    }

    /**
     * This method returns true if the storageBin has enough space to retrieve the newDrivingOrders
     * depending on the already existing OUTPUT drivingOrders.
     *
     * @param storageBinToCheck contains the amount of stored items
     * @param newDrivingOrder   contains the amount that should be retrieved from the storageBin
     * @return boolean
     */

    public boolean hasEnoughAmount(StorageBin storageBinToCheck, NewDrivingOrder newDrivingOrder) {

        int actualStorageBinAmount = storageBinToCheck.amount();

        int itemsToRetrieve = newDrivingOrder.amount();

        List<DrivingOrder> existingOutputDrivingOrders = drivingOrderRepo.findByTypeAndStorageLocationId(Type.OUTPUT, storageBinToCheck.locationId());

        int ordersTotalAmount = getTotalAmountFromList(existingOutputDrivingOrders);

        int remainingAmount = actualStorageBinAmount - ordersTotalAmount;

        return remainingAmount >= itemsToRetrieve;

    }

    /**
     * This method makes the storageBinService put the amount of INPUT drivingOrder in the storage bin.
     * deletes the drivingOrder in the database.
     *
     * @param id is the id of the INPUT drivingOrder
     */
    public void inputDrivingOrderDone(String id) {
        Optional<DrivingOrder> succeedInputDrivingOrder = drivingOrderRepo.findById(id);

        if (succeedInputDrivingOrder.isEmpty()) {
            throw new OrderToDeleteNotFoundException(id);
        }
        storageBinService.updateInputStorageBin(succeedInputDrivingOrder.get());
        drivingOrderRepo.deleteById(id);
    }

    /**
     * Makes the StorageBinService update the amount of OUTPUT drivingOrder.
     * Uses the beforeDoneControl method to check if the StorageBin is empty.
     * deletes the drivingOrder in the database.
     *
     * @param id is the id of the INPUT drivingOrder
     */
    public void outputDrivingOrderDone(String id) {
        Optional<DrivingOrder> succeedOutputDrivingOrder = drivingOrderRepo.findById(id);

        if (succeedOutputDrivingOrder.isEmpty()) {
            throw new OrderToDeleteNotFoundException(id);
        }
        boolean storageBinIsEmpty = beforeDoneControl(succeedOutputDrivingOrder.get());

        storageBinService.updateOutputStorageBin(storageBinIsEmpty, succeedOutputDrivingOrder.get());

        drivingOrderRepo.deleteById(id);

    }

    /**
     * This method checks if the StorageBin is empty before retrieving the newDrivingOrder amount.
     * returns true if the StorageBin will be empty
     * returns false if the storageBin has still amount
     *
     * @param succeedOutputDrivingOrder contains the amount that will be retrieved from the storageBin
     * @return boolean
     */

    public boolean beforeDoneControl(DrivingOrder succeedOutputDrivingOrder) {
        StorageBin storageBinToControl = storageBinService.findStorageBinByLocationId(succeedOutputDrivingOrder.storageLocationId());
        if ((storageBinToControl.amount() - succeedOutputDrivingOrder.amount()) < 0) {
            throw new NotEnoughItemsRemainingException(succeedOutputDrivingOrder.storageLocationId());
        }
        return (storageBinToControl.amount() - succeedOutputDrivingOrder.amount()) == 0;
    }
}
