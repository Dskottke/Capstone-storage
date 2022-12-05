package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DrivingOrderRepo extends MongoRepository<DrivingOrder, String> {
   List<DrivingOrder> findByType(Type type);

   List<DrivingOrder> findByTypeAndStorageBinNumber(Type type, String storageBinNumber);
}
