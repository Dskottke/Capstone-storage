package capstone.storage.backend.drivingorders;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/driving-order")
@RequiredArgsConstructor
public class DrivingOrderController {
    private final DrivingOrderService drivingOrderService;

    @GetMapping
    public List<DrivingOrder> getAllDrivingOrdersByType(@RequestParam Type type) {
        return drivingOrderService.getAllDrivingOrdersByType(type);
    }
}
