package capstone.storage.backend.item;

import capstone.storage.backend.item.models.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ItemRepo extends MongoRepository<Item, String> {
    boolean existsByEan(String ean);

    boolean existsByItemNumber(int itemNumber);


    Optional<Item> findItemByItemNumber(int itemNumber);
}
