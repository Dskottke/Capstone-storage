package capstone.storage.backend.testdata;

import capstone.storage.backend.exceptions.TestDataItemsNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.models.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static String readFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public void addItemData() throws IOException {

        Class<Item> itemClass = Item.class;
        InputStream inputStream = itemClass.getResourceAsStream("/item.json");
        String data = readFromInputStream(inputStream);
        List<Item> itemList = objectMapper.readValue(data, new TypeReference<List<Item>>() {
        });

        for (Item itemToAdd : itemList) {
            itemRepo.insert(itemToAdd);
        }
    }

    public void deleteAll() {
        itemRepo.deleteAll();
    }

}
