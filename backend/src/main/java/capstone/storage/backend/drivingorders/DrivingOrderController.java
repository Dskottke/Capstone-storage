package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.exceptions.IsNullOrEmptyException;
import capstone.storage.backend.exceptions.ItemToDeleteNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/driving-orders")
@RequiredArgsConstructor
public class DrivingOrderController {
    private final DrivingOrderService drivingOrderService;

    @GetMapping
    public List<DrivingOrder> getAllDrivingOrdersByType(@RequestParam Type type) {
        return drivingOrderService.getAllDrivingOrdersByType(type);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/input")
    public DrivingOrder addNewInputDrivingOrder(@RequestBody Optional<NewDrivingOrder> drivingOrderToAdd) {
        if (drivingOrderToAdd.isEmpty() || drivingOrderService.isNullOrEmpty(drivingOrderToAdd.get())) {
            throw new IsNullOrEmptyException();
        }
        return drivingOrderService.addNewInputDrivingOrder(drivingOrderToAdd.get());
    }

    @DeleteMapping("/input/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void drivingOrderDone(@PathVariable String id) {
        if (!drivingOrderService.existById(id)) {
            throw new ItemToDeleteNotFoundException();
        }
    }
}
