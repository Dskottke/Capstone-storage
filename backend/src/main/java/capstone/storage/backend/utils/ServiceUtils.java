package capstone.storage.backend.utils;

import capstone.storage.backend.item.models.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Component
public class ServiceUtils {
    ObjectMapper objectMapper = new ObjectMapper();

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public List<Item> getListFromItemData() throws IOException {
        Class<Item> itemClass = Item.class;
        InputStream inputStream = itemClass.getResourceAsStream("/item.json");
        return objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }
}

