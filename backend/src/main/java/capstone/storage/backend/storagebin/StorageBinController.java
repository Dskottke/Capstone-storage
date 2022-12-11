package capstone.storage.backend.storagebin;

import capstone.storage.backend.storagebin.models.StorageBinReturn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/storagebins/")
@RequiredArgsConstructor
public class StorageBinController {
    private final StorageBinService storageBinService;

    @GetMapping
    public List<StorageBinReturn> getAllStorageBins() {
        return storageBinService.getAllStorageBins();
    }
}
