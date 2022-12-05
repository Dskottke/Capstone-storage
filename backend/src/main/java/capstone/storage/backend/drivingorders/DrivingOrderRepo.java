package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DrivingOrderRepo extends MongoRepository<DrivingOrder, String> {
    List<DrivingOrder> findByType(Type type);

    List<DrivingOrder> findByTypeAndStorageBinNumber(Type type, String storageBinNumber);

    Optional<DrivingOrder> findFirstByStorageBinNumber(String storageBinNumber);
}
