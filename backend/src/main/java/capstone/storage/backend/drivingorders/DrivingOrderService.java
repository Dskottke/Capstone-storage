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

    public DrivingOrder addNewDrivingOrder(Type type, NewDrivingOrder newDrivingOrder) {
        validateNewDrivingOrder(type, newDrivingOrder);
        DrivingOrder drivingOrder = new DrivingOrder(
                serviceUtils.generateUUID(),
                newDrivingOrder.storageLocationId(),
                newDrivingOrder.itemNumber(), type,
                newDrivingOrder.amount());
        return drivingOrderRepo.insert(drivingOrder);
    }

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

    private void validateOutputOrder(StorageBin storageBinFromOrder, Item itemFromOrder, NewDrivingOrder newDrivingOrder) {
        if (!checkOutputValidation(storageBinFromOrder, itemFromOrder)) {
            throw new StorageBinFalseItemException();
        }
        if (!hasEnoughAmount(storageBinFromOrder, newDrivingOrder)) {
            throw new NotEnoughItemsRemainingException();
        }
    }

    private void validateInputOrder(StorageBin storageBinFromOrder, Item itemFromOrder, NewDrivingOrder newDrivingOrder) {
        if (!isValidInputItem(storageBinFromOrder, itemFromOrder.itemNumber())) {
            throw new StorageBinFalseItemException();
        }
        if (!hasFreeAmount(storageBinFromOrder, newDrivingOrder, itemFromOrder)) {
            throw new IsNotEnoughSpaceException();
        }
    }

    public boolean isNullOrEmpty(NewDrivingOrder newDrivingOrder) {
        if (newDrivingOrder.storageLocationId() == null || newDrivingOrder.itemNumber() == 0 || newDrivingOrder.amount() == 0) {
            return true;
        }
        String emptyString = "";
        return emptyString.equals(newDrivingOrder.storageLocationId());
    }

    private boolean itemAndStorageBinExisting(NewDrivingOrder newDrivingOrder) {
        return itemService.existByItemNumber(newDrivingOrder.itemNumber()) && storageBinService.existsByLocationId(newDrivingOrder.storageLocationId());
    }

    public boolean checkOutputValidation(StorageBin storageBinToCheck, Item itemToCheck) {
        return storageBinToCheck.itemNumber() == itemToCheck.itemNumber();
    }

    public boolean isValidInputItem(StorageBin storageBinToCheck, int itemNumber) {
        boolean binIsNotOccupiedByOtherItem = storageBinToCheck.itemNumber() == itemNumber || storageBinToCheck.itemNumber() == 0;
        return binIsNotOccupiedByOtherItem && !hasConflictingOrder(storageBinToCheck, itemNumber);
    }

    private boolean hasConflictingOrder(StorageBin storageBin, int itemNumber) {
        return drivingOrderRepo.findFirstByStorageLocationIdAndType(storageBin.locationId(), Type.INPUT)
                .map(drivingOrder -> drivingOrder.itemNumber() != itemNumber)
                .orElse(false);
    }

    public int getTotalAmountFromList(List<DrivingOrder> existingInputDrivingOrders) {
        AtomicInteger orderTotalAmount = new AtomicInteger();
        existingInputDrivingOrders.forEach(drivingOrder -> orderTotalAmount.addAndGet(drivingOrder.amount()));
        return orderTotalAmount.get();
    }

    public boolean hasFreeAmount(StorageBin storageBinToCheck, NewDrivingOrder newDrivingOrder, Item itemToCheck) {

        int actualStorageBinAmount = storageBinToCheck.amount();

        int itemsToStoreAmount = newDrivingOrder.amount();

        int storageBinCapacity = itemToCheck.storableValue();

        List<DrivingOrder> existingInputDrivingOrders = drivingOrderRepo.findByTypeAndStorageLocationId(Type.INPUT, storageBinToCheck.locationId());

        int ordersTotalAmount = getTotalAmountFromList(existingInputDrivingOrders);

        int freeAmount = storageBinCapacity - (ordersTotalAmount + actualStorageBinAmount);

        return itemsToStoreAmount <= freeAmount;
    }

    public boolean hasEnoughAmount(StorageBin storageBinToCheck, NewDrivingOrder newDrivingOrder) {

        int actualStorageBinAmount = storageBinToCheck.amount();

        int itemsToRetrieve = newDrivingOrder.amount();

        List<DrivingOrder> existingOutputDrivingOrders = drivingOrderRepo.findByTypeAndStorageLocationId(Type.OUTPUT, storageBinToCheck.locationId());

        int ordersTotalAmount = getTotalAmountFromList(existingOutputDrivingOrders);

        int remainingAmount = actualStorageBinAmount - ordersTotalAmount;

        return remainingAmount >= itemsToRetrieve;

    }

    public void inputDrivingOrderDone(String id) {
        Optional<DrivingOrder> succeedInputDrivingOrder = drivingOrderRepo.findById(id);

        if (succeedInputDrivingOrder.isEmpty()) {
            throw new OrderToDeleteNotFoundException();
        }
        storageBinService.updateInputStorageBin(succeedInputDrivingOrder.get());
        drivingOrderRepo.deleteById(id);
    }

    public void outputDrivingOrderDone(String id) {
        Optional<DrivingOrder> succeedOutputDrivingOrder = drivingOrderRepo.findById(id);

        if (succeedOutputDrivingOrder.isEmpty()) {
            throw new OrderToDeleteNotFoundException();
        }
        boolean storageBinIsEmpty = beforeDoneControl(succeedOutputDrivingOrder.get());

        storageBinService.updateOutputStorageBin(storageBinIsEmpty, succeedOutputDrivingOrder.get());

        drivingOrderRepo.deleteById(id);

    }

    public boolean beforeDoneControl(DrivingOrder succeedOutputDrivingOrder) {
        StorageBin storageBinToControl = storageBinService.findStorageBinByLocationId(succeedOutputDrivingOrder.storageLocationId());
        if ((storageBinToControl.amount() - succeedOutputDrivingOrder.amount()) < 0) {
            throw new NotEnoughItemsRemainingException();
        }
        return (storageBinToControl.amount() - succeedOutputDrivingOrder.amount()) == 0;
    }
}
