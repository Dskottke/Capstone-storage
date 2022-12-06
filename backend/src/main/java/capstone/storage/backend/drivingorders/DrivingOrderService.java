package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.exceptions.*;
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

    public DrivingOrder addNewDrivingOrder(Type type, NewDrivingOrder newDrivingOrder) {
        //gemeinsam
        //überprüfen ob das lager und das item existieren
        if (!itemAndStorageBinExisting(newDrivingOrder)) {
            throw new ItemOrStorageBinNotExistingException();
        }
        //das item rausholen
        Item itemFromOrder = itemService.findItemByItemNumber(newDrivingOrder.itemNumber());
        // das Lager rausholen
        StorageBin storageBinFromOrder = storageBinService.findStorageBinByLocationId(newDrivingOrder.storageLocationId());

        //input
        if (type.equals(Type.INPUT)) {

            if (!checkInputValidation(storageBinFromOrder, itemFromOrder)) {
                throw new StorageBinFalseItemException();
            }

            if (!checkInputStorageBinFreeAmount(storageBinFromOrder, newDrivingOrder, itemFromOrder)) {
                throw new IsNotEnoughSpaceException();
            }

            return drivingOrderRepo.insert(new DrivingOrder(serviceUtils.generateUUID(),
                    newDrivingOrder.storageLocationId(),
                    newDrivingOrder.itemNumber(),
                    Type.INPUT,
                    newDrivingOrder.amount()));
        }

        //output
        //überprüfen ob das item auf dem Lager mit dem item übereinstimmt
        if (!checkOutputValidation(storageBinFromOrder, itemFromOrder)) {
            throw new StorageBinFalseItemException();
        }
        //überprüfen in abhängigkeit der vorhandenen Aufträge ob die Restmenge reicht
        if (!checkOutputStorageBinEnoughAmount(storageBinFromOrder, newDrivingOrder)) {
            throw new NotEnoughItemsRemainingException();
        }
        // auftrag speichern
        return drivingOrderRepo.insert(new DrivingOrder(serviceUtils.generateUUID(),
                newDrivingOrder.storageLocationId(),
                newDrivingOrder.itemNumber(),
                Type.OUTPUT,
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

    public boolean checkOutputValidation(StorageBin storageBinToCheck, Item itemToCheck) {
        return storageBinToCheck.itemNumber().equals(itemToCheck.itemNumber());
    }

    public boolean checkInputValidation(StorageBin storageBinToCheck, Item itemToCheck) {
        //überprüfen ob es einen Auftrag gibt für den Lagerplatz und den holen
        //HIER MUSS WAS GEÄNDERT WERDEN ER MUSS NACH DEM TYPE SUCHEN // SOLLTE ERLEDIGT SEIN <-
        Optional<DrivingOrder> existingOrderMatchingStorageBin = drivingOrderRepo.findFirstByStorageLocationIdAndType(storageBinToCheck.locationId(), Type.INPUT);
        //wenn dieser existiert dann fragen ob das item auf dem lagerplatz mit dem geholten item übereinstimmt
        if (existingOrderMatchingStorageBin.isPresent() && (!existingOrderMatchingStorageBin.get().itemNumber().equals(itemToCheck.itemNumber()))) {
            return false;
        }
        //gucken ob die itemNumber vom lager mit der itemNumber vom auftrag übereinstimmt oder der lagerplatz 0 enthält
        return storageBinToCheck.itemNumber().equals(itemToCheck.itemNumber()) || storageBinToCheck.itemNumber().equals("0");
    }

    public int getTotalAmountFromList(List<DrivingOrder> existingInputDrivingOrders) {
        AtomicInteger orderTotalAmount = new AtomicInteger();
        existingInputDrivingOrders.forEach(drivingOrder -> orderTotalAmount.addAndGet(Integer.parseInt(drivingOrder.amount())));
        return orderTotalAmount.get();
    }

    public boolean checkInputStorageBinFreeAmount(StorageBin storageBinToCheck, NewDrivingOrder newDrivingOrder, Item itemToCheck) {

        int actualStorageBinAmount = Integer.parseInt(storageBinToCheck.amount());

        int itemsToStore = Integer.parseInt(newDrivingOrder.amount());

        int storageBinCapacity = Integer.parseInt(itemToCheck.storableValue());

        List<DrivingOrder> existingInputDrivingOrders = drivingOrderRepo.findByTypeAndStorageLocationId(Type.INPUT, storageBinToCheck.locationId());

        int ordersTotalAmount = getTotalAmountFromList(existingInputDrivingOrders);

        int freeAmount = storageBinCapacity - (ordersTotalAmount + actualStorageBinAmount);

        return itemsToStore <= freeAmount;
    }

    public boolean checkOutputStorageBinEnoughAmount(StorageBin storageBinToCheck, NewDrivingOrder newDrivingOrder) {

        // aktuelle lagermenge
        int actualStorageBinAmount = Integer.parseInt(storageBinToCheck.amount());
        //neuer auftragsbetrag
        int itemsToRetrieve = Integer.parseInt(newDrivingOrder.amount());
        //hol mir alle existierenden output aufträge für den Lagerplatz
        List<DrivingOrder> existingOutputDrivingOrders = drivingOrderRepo.findByTypeAndStorageLocationId(Type.OUTPUT, storageBinToCheck.locationId());
        // die Menge aller auftragsbeträge
        int ordersTotalAmount = getTotalAmountFromList(existingOutputDrivingOrders);
        //aktuelle Lagermenge - die menge aller auftragsbeträge
        int remainingAmount = actualStorageBinAmount - ordersTotalAmount;
        //wenn die Restmenge größer gleich der Auftragsmenge ist dann true sonst false
        return remainingAmount >= itemsToRetrieve;

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
