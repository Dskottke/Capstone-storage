package capstone.storage.backend.storagebin;

import capstone.storage.backend.storagebin.models.StorageBin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StorageBinRepo extends MongoRepository<StorageBin, String> {
    boolean existsByLocationId(String id);

    boolean existsByItemNumber(int itemNumber);

    StorageBin findStorageBinByLocationId(String locationId);

    List<StorageBin> findAllByItemNumber(int itemNumber);
}
