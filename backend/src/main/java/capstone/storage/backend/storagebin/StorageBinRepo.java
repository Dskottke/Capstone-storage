package capstone.storage.backend.storagebin;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorageBinRepo extends MongoRepository<StorageBin, String> {
    boolean existsByLocationId(String id);

    StorageBin findItemByLocationId(String storageBinNumber);
}
