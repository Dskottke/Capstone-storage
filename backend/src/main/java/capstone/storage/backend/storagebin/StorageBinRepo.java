package capstone.storage.backend.storagebin;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorageBinRepo extends MongoRepository<StorageBin, String> {
    boolean existsByLocationNumber(String id);

    StorageBin findItemByLocationNumber(String storageBinNumber);
}
