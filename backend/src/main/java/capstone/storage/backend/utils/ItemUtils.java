package capstone.storage.backend.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component

public class ItemUtils {


    public String generateUUID() {
        return UUID.randomUUID().toString();
    }


}
