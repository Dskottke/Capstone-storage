package capstone.storage.backend.testdata;

import capstone.storage.backend.exceptions.TestDataItemsNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.StorageBin;
import capstone.storage.backend.storagebin.StorageBinRepo;
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


    public void addTestData() {
        deleteAll();
        try {
            List<Item> testItemListToAdd = serviceUtils.parseListFromJson(new TypeReference<>() {
            }, ResourcePath.ITEM_PATH.toString());
            addListToItemDB(testItemListToAdd);
            List<StorageBin> testStorageBinToAdd = serviceUtils.parseListFromJson(new TypeReference<>() {
            }, ResourcePath.STORAGE_PATH.toString());
            addListToStorageDB(testStorageBinToAdd);
        } catch (IOException e) {
            throw new TestDataItemsNotFoundException("can't find data from json");
        }
    }

    public void deleteAll() {
        itemRepo.deleteAll();
        storageBinRepo.deleteAll();
    }

    public List<Item> addListToItemDB(List<Item> testItemListToAdd) {
        testItemListToAdd.forEach(itemRepo::insert);
        return testItemListToAdd;
    }

    public List<StorageBin> addListToStorageDB(List<StorageBin> testItemListToAdd) {
        testItemListToAdd.forEach(storageBinRepo::insert);
        return testItemListToAdd;
    }
}
