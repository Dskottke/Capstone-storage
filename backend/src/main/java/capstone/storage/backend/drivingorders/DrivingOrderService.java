package capstone.storage.backend.drivingorders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class DrivingOrderService {
    private final DrivingOrderRepo drivingOrderRepo;

    public List<DrivingOrder> getAllInputDrivingOrders() {
        drivingOrderRepo.findAllByDrivingOrderTypeIsInput();
    }
}
