package capstone.storage.backend.storagebin;

import capstone.storage.backend.item.ItemRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StorageBinServiceTest {

    private final StorageBinRepo storageBinrepo = mock(StorageBinRepo.class);
    private final ItemRepo itemRepo = mock(ItemRepo.class);
    private final StorageBinService service = new StorageBinService(storageBinrepo, itemRepo);

    @Test
    @DisplayName("method -> getAllStorageBins should return the expected list")
    void getAllStorageBinsAndReturnExpectedList() {
        //GIVEN
        List<StorageBin> expectedList = List.of(
                new StorageBin("1", "1", "1", "20", ""));
        //WHEN
        when(storageBinrepo.findAll()).thenReturn(expectedList);
        List<StorageBin> actual = service.getAllStorageBins();
        //THEN
        assertEquals(expectedList, actual);
    }

}
