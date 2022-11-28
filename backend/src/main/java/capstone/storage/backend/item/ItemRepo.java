package capstone.storage.backend.item;

import capstone.storage.backend.item.models.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepo extends MongoRepository<Item, String> {
    boolean existsByEan(String ean);

    boolean existsByItemNumber(String itemNumber);
}
