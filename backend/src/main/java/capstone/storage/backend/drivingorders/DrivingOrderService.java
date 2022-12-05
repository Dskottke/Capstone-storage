package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.exceptions.IsNotEnoughSpaceException;
import capstone.storage.backend.exceptions.ItemOrStorageBinNotExistingException;
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

        if (!fieldsExisting(newDrivingOrder)) {
            throw new ItemOrStorageBinNotExistingException();
        }

        Item itemToCheck = itemService.findItemByItemNumber(newDrivingOrder.itemNumber());
        StorageBin storageBinToCheck = storageBinService.findStorageBinByLocationNumber(newDrivingOrder.storageLocationNumber());

        if (!checkStorageBinValid(storageBinToCheck, itemToCheck)) {
            throw new StorageBinFalseItemException();
        }

        if (!checkInputDrivingOrderIsValid(storageBinToCheck, newDrivingOrder, itemToCheck)) {
            throw new IsNotEnoughSpaceException();
        }

        return drivingOrderRepo.insert(new DrivingOrder(serviceUtils.generateUUID(),
                newDrivingOrder.storageLocationNumber(),
                newDrivingOrder.itemNumber(),
                Type.INPUT,
                newDrivingOrder.amount()));
    }

    public boolean isNullOrEmpty(NewDrivingOrder newDrivingOrder) {
        if (newDrivingOrder.storageLocationNumber() == null
                || newDrivingOrder.itemNumber() == null
                || newDrivingOrder.amount() == null) {
            return true;
        }
        String emptyString = "";
        return emptyString.equals(newDrivingOrder.itemNumber())
                || emptyString.equals(newDrivingOrder.storageLocationNumber())
                || emptyString.equals(newDrivingOrder.amount());
    }

    public boolean fieldsExisting(NewDrivingOrder newDrivingOrder) {
        return itemService.existByItemNumber(newDrivingOrder.itemNumber())
                || storageBinService.existsByLocationNumber(newDrivingOrder.storageLocationNumber());
    }

    public boolean checkStorageBinValid(StorageBin storageBinToCheck, Item itemToCheck) {

        Optional<DrivingOrder> existingOrder = drivingOrderRepo.findFirstByStorageBinNumber(storageBinToCheck.locationNumber());
        if (existingOrder.isPresent() && (!existingOrder.get().itemNumber().equals(itemToCheck.itemNumber()))) {
            return false;
        }
        return storageBinToCheck.itemNumber().equals(itemToCheck.itemNumber()) || storageBinToCheck.itemNumber().equals("0");
    }

    public int getTotalAmountFromList(List<DrivingOrder> existingInputDrivingOrders) {
        AtomicInteger orderTotalAmount = new AtomicInteger();
        existingInputDrivingOrders.forEach(drivingOrder -> orderTotalAmount.addAndGet(Integer.parseInt(drivingOrder.amount())));
        return orderTotalAmount.get();
    }

    public boolean checkInputDrivingOrderIsValid(StorageBin storageBinToCheck, NewDrivingOrder newDrivingOrder, Item itemToCheck) {
        int actualStorageBinAmount = Integer.parseInt(storageBinToCheck.amount());

        int itemsToStore = Integer.parseInt(newDrivingOrder.amount());

        int storageBinCapacity = Integer.parseInt(itemToCheck.storableValue());

        List<DrivingOrder> existingInputDrivingOrders = drivingOrderRepo.findByTypeAndStorageBinNumber(Type.INPUT, storageBinToCheck.locationNumber());

        int ordersTotalAmount = getTotalAmountFromList(existingInputDrivingOrders);

        int freeAmount = storageBinCapacity - (ordersTotalAmount + actualStorageBinAmount);

        return itemsToStore <= freeAmount;
    }
}
