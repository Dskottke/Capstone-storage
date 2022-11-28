package capstone.storage.backend.testdata;

import capstone.storage.backend.exceptions.TestDataItemsNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.models.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("item.json")).getFile());


        List<Item> itemList = objectMapper.readValue(
                file,
                new TypeReference<List<Item>>() {
                });

        for (Item itemToAdd : itemList) {
            itemRepo.insert(itemToAdd);
        }
    }

    public void deleteAll() {
        itemRepo.deleteAll();
    }

}
