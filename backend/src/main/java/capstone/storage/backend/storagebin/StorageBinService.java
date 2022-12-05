package capstone.storage.backend.storagebin;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageBinService {
    private final StorageBinRepo repo;


    public StorageBin findStorageBinByLocationId(String storageBinNumber) {
        return repo.findItemByLocationId(storageBinNumber);
    }

    public List<StorageBin> getAllStorageBins() {
        return repo.findAll();
    }

    public boolean existsByLocationNumber(String locationNumber) {
        return repo.existsByLocationId(locationNumber);
    }

    public void updateStorageBin(DrivingOrder drivingOrder) {
        StorageBin storageBinToUpdate = repo.findById(drivingOrder.storageBinId())
                .orElseThrow();

        int newAmount = Integer.parseInt(storageBinToUpdate.amount()) + Integer.parseInt(drivingOrder.amount());

        StorageBin update = new StorageBin(
                storageBinToUpdate.id(),
                drivingOrder.storageBinId(),
                drivingOrder.itemNumber(),
                Integer.toString(newAmount));

        repo.save(update);
    }
}

