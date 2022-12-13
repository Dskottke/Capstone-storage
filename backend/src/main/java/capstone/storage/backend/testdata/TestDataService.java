package capstone.storage.backend.testdata;

import capstone.storage.backend.drivingorders.DrivingOrderRepo;
import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.exceptions.TestDataItemsNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.StorageBinRepo;
import capstone.storage.backend.storagebin.models.StorageBin;
import capstone.storage.backend.utils.ServiceUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDataService {
    private final ItemRepo itemRepo;
    private final StorageBinRepo storageBinRepo;
    private final ServiceUtils serviceUtils;

    private final DrivingOrderRepo drivingOrderRepo;

    /**
     * this method deletes all Items,StorageBins and DrivingOrders from the database,
     * manages the parsing of Objects(Items,StorageBins and DrivingOrders) from JSON data
     * and the storing of these objects in the database.
     */
    public void addTestData() {
        deleteAll();
        try {
            List<Item> testItemListToAdd = serviceUtils.parseListFromJson(new TypeReference<>() {
            }, ResourcePath.ITEM_PATH.toString());
            addListToItemDB(testItemListToAdd);
            List<StorageBin> testStorageBinsToAdd = serviceUtils.parseListFromJson(new TypeReference<>() {
            }, ResourcePath.STORAGE_PATH.toString());
            addListToStorageDB(testStorageBinsToAdd);
            List<DrivingOrder> testDrivingOrdersToAdd = serviceUtils.parseListFromJson(new TypeReference<>() {
            }, ResourcePath.DRIVING_ORDER_PATH.toString());
            addListToDrivingOrderDB(testDrivingOrdersToAdd);
        } catch (IOException e) {
            throw new TestDataItemsNotFoundException();
        }
    }

    public void deleteAll() {
        itemRepo.deleteAll();
        storageBinRepo.deleteAll();
        drivingOrderRepo.deleteAll();
    }

    public List<Item> addListToItemDB(List<Item> testItemListToAdd) {
        testItemListToAdd.forEach(itemRepo::insert);
        return testItemListToAdd;
    }

    public List<StorageBin> addListToStorageDB(List<StorageBin> testItemListToAdd) {
        testItemListToAdd.forEach(storageBinRepo::insert);
        return testItemListToAdd;
    }

    public List<DrivingOrder> addListToDrivingOrderDB(List<DrivingOrder> testDrivingOrdersToAdd) {
        testDrivingOrdersToAdd.forEach(drivingOrderRepo::insert);
        return testDrivingOrdersToAdd;
    }
}
