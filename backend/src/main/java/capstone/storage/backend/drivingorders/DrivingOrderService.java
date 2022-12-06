package capstone.storage.backend.drivingorders;
import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.exceptions.IsNotEnoughSpaceException;
import capstone.storage.backend.exceptions.ItemOrStorageBinNotExistingException;
import capstone.storage.backend.exceptions.OrderToDeleteNotFoundException;
import capstone.storage.backend.exceptions.StorageBinFalseItemException;
import capstone.storage.backend.item.ItemService;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.StorageBin;
import capstone.storage.backend.storagebin.StorageBinService;
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

    public DrivingOrder addNewInputDrivingOrder(NewDrivingOrder newDrivingOrder) {

        if (!itemAndStorageBinExisting(newDrivingOrder)) {
            throw new ItemOrStorageBinNotExistingException();
        }

        Item itemFromOrder = itemService.findItemByItemNumber(newDrivingOrder.itemNumber());
        StorageBin storageBinFromOrder = storageBinService.findStorageBinByLocationId(newDrivingOrder.storageLocationId());

        if (!checkValidation(storageBinFromOrder, itemFromOrder)) {
            throw new StorageBinFalseItemException();
        }

        if (!checkStorageBinFreeAmount(storageBinFromOrder, newDrivingOrder, itemFromOrder)) {
            throw new IsNotEnoughSpaceException();
        }

        return drivingOrderRepo.insert(new DrivingOrder(serviceUtils.generateUUID(),
                newDrivingOrder.storageLocationId(),
                newDrivingOrder.itemNumber(),
                Type.INPUT,
                newDrivingOrder.amount()));
    }

    public boolean isNullOrEmpty(NewDrivingOrder newDrivingOrder) {
        if (newDrivingOrder.storageLocationId() == null
                || newDrivingOrder.itemNumber() == null
                || newDrivingOrder.amount() == null) {
            return true;
        }
        String emptyString = "";
        return emptyString.equals(newDrivingOrder.itemNumber())
                || emptyString.equals(newDrivingOrder.storageLocationId())
                || emptyString.equals(newDrivingOrder.amount());
    }

    public boolean itemAndStorageBinExisting(NewDrivingOrder newDrivingOrder) {
        return itemService.existByItemNumber(newDrivingOrder.itemNumber())
                || storageBinService.existsByLocationId(newDrivingOrder.storageLocationId());
    }

    public boolean checkValidation(StorageBin storageBinToCheck, Item itemToCheck) {
        Optional<DrivingOrder> existingOrderMatchingStorageBin = drivingOrderRepo.findFirstByStorageLocationId(storageBinToCheck.locationId());
        if (existingOrderMatchingStorageBin.isPresent() && (!existingOrderMatchingStorageBin.get().itemNumber().equals(itemToCheck.itemNumber()))) {
            return false;
        }
        return storageBinToCheck.itemNumber().equals(itemToCheck.itemNumber()) || storageBinToCheck.itemNumber().equals("0");
    }

    public int getTotalAmountFromList(List<DrivingOrder> existingInputDrivingOrders) {
        AtomicInteger orderTotalAmount = new AtomicInteger();
        existingInputDrivingOrders.forEach(drivingOrder -> orderTotalAmount.addAndGet(Integer.parseInt(drivingOrder.amount())));
        return orderTotalAmount.get();
    }

    public boolean checkStorageBinFreeAmount(StorageBin storageBinToCheck, NewDrivingOrder newDrivingOrder, Item itemToCheck) {

        int actualStorageBinAmount = Integer.parseInt(storageBinToCheck.amount());

        int itemsToStore = Integer.parseInt(newDrivingOrder.amount());

        int storageBinCapacity = Integer.parseInt(itemToCheck.storableValue());

        List<DrivingOrder> existingInputDrivingOrders = drivingOrderRepo.findByTypeAndStorageLocationId(Type.INPUT, storageBinToCheck.locationId());

        int ordersTotalAmount = getTotalAmountFromList(existingInputDrivingOrders);

        int freeAmount = storageBinCapacity - (ordersTotalAmount + actualStorageBinAmount);

        return itemsToStore <= freeAmount;
    }

    public void drivingOrderDone(String id) {
        Optional<DrivingOrder> succeedDrivingOrder = drivingOrderRepo.findById(id);

        if (succeedDrivingOrder.isEmpty()) {
            throw new OrderToDeleteNotFoundException();
        }
        storageBinService.updateStorageBin(succeedDrivingOrder.get());
        drivingOrderRepo.deleteById(id);
    }
}
