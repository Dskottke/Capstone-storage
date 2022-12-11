package capstone.storage.backend.storagebin;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StorageBinRepo extends MongoRepository<StorageBin, String> {
    boolean existsByLocationId(String id);

    boolean existsStorageBinsByItemNumber(String itemNumber);

    StorageBin findStorageBinByLocationId(String storageBinNumber);

    List<StorageBin> findAllByItemNumber(String itemNumber);
}
