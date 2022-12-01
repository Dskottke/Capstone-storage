package capstone.storage.backend.drivingorders;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DrivingOrderRepo extends MongoRepository<DrivingOrder, String> {
   List<DrivingOrder> findAllByDrivingOrderTypeIsInput();
}
