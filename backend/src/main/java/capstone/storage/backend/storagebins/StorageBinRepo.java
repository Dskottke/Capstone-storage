package capstone.storage.backend.storagebins;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorageBinRepo extends MongoRepository<StorageBin, String> {
}
