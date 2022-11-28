package capstone.storage.backend.testdata;

import capstone.storage.backend.exceptions.TestDataItemsNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.models.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDataService {
    private final ItemRepo itemRepo;
    private final ObjectMapper objectMapper;

    public void addTestData() {
        deleteAll();
        try {
            addItemData();
        } catch (IOException e) {
            throw new TestDataItemsNotFoundException("cant find data from json");
        }


    }

    public void addItemData() throws IOException {

        List<Item> itemList = Collections.singletonList(objectMapper.readValue(new URL(
                "file:src/main/java/capstone/storage/backend/testdata/json/item.json"), Item.class));
        for (Item itemToAdd : itemList) {
            itemRepo.insert(itemToAdd);
        }
    }

    public void deleteAll() {
        itemRepo.deleteAll();
    }

}
