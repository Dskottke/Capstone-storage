package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class DrivingOrderService {
    private final DrivingOrderRepo drivingOrderRepo;
    private final ServiceUtils serviceUtils;

    public List<DrivingOrder> getAllDrivingOrdersByType(Type type) {
        return drivingOrderRepo.findByType(type);
    }

    public DrivingOrder addNewInputDrivingOrder(NewDrivingOrder newDrivingOrder) {
        return new DrivingOrder(serviceUtils.generateUUID(),
                newDrivingOrder.storageBinNumber(),
                newDrivingOrder.itemNumber(),
                Type.INPUT,
                newDrivingOrder.amount());
    }

    public boolean isNullOrEmpty(NewDrivingOrder newDrivingOrder) {
        if (newDrivingOrder.storageBinNumber() == null
                || newDrivingOrder.itemNumber() == null
                || newDrivingOrder.amount() == null) {
            return true;
        }
        String emptyString = "";
        return emptyString.equals(newDrivingOrder.itemNumber())
                || emptyString.equals(newDrivingOrder.storageBinNumber())
                || emptyString.equals(newDrivingOrder.amount());
    }
}
