package capstone.storage.backend.storagebin;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageBinService {
    private final StorageBinRepo repo;

    public StorageBin findStorageBinByLocationNumber(String storageBinNumber) {
        return repo.findItemByLocationNumber(storageBinNumber);
    }

    public List<StorageBin> getAllStorageBins() {
        return repo.findAll();
    }

    public boolean existsByLocationNumber(String locationNumber) {
        return repo.existsByLocationNumber(locationNumber);
    }

    public void updateStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdate = new StorageBin(
                drivingOrder.storageBinNumber(),
                drivingOrder.itemNumber(),
                drivingOrder.amount());

        repo.save(storageBinToUpdate);
    }
}

