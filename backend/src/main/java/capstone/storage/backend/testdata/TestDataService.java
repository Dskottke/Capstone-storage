package capstone.storage.backend.testdata;

import capstone.storage.backend.exceptions.TestDataItemsNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
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
            List<Item> testItemListToAdd = serviceUtils.getListFromJson(new TypeReference<List<Item>>() {
            });
            addListToItemDB(testItemListToAdd);
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
}
