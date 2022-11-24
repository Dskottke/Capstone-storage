package capstone.storage.backend;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepo extends MongoRepository<Item, String> {
 boolean existsByEan(String ean);

 boolean existsByItemNumber(String itemNumber);
}
