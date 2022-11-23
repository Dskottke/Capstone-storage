package capstone.storage.backend.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ServiceUtils {
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
