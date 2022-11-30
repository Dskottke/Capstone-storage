package capstone.storage.backend.utils;

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

    public <T> List<T> getListFromJson(TypeReference<List<T>> classTypeReference) throws IOException {
        Class<?> itemClass = getClass();
        InputStream inputStream = itemClass.getResourceAsStream("/item.json");
        return objectMapper.readValue(inputStream, classTypeReference);
    }
}
