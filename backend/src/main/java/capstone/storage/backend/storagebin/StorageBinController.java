package capstone.storage.backend.storagebin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/storage/")
@RequiredArgsConstructor
public class StorageBinController {
    private final StorageBinService storageBinService;
    @GetMapping()
    public List<StorageBin> getAllStorageBins() {
        return storageBinService.getAllStorageBins();
    }
}
