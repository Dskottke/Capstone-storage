package capstone.storage.backend.testdata;

import capstone.storage.backend.exceptions.TestDataItemsNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.StorageBinRepo;
import capstone.storage.backend.utils.ServiceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TestDataServiceTest {

    ServiceUtils serviceUtils = mock(ServiceUtils.class);
    StorageBinRepo storageBinRepo = mock(StorageBinRepo.class);
    ItemRepo itemRepo = mock(ItemRepo.class);
    private final TestDataService testDataService = new TestDataService(itemRepo, storageBinRepo, serviceUtils);

    @Test
    @DisplayName("method -> should return the given list")
    void addListToItemDBAndExpectList() {
        //GIVEN
        Item listItem = new Item(
                "992901d9-5eb8-4992-9620-e5e80bb7f0e0",
                "Axe Bodyspray Wasabi & Fresh Linen",
                "Unknown",
                "NL",
                "8710847909610",
                "20",
                "1");
        List<Item> expected = List.of(listItem);
        //WHEN
        List<Item> actual = testDataService.addListToItemDB(expected);
        //THEN
        assertEquals(expected, actual);
    }
    @Test
    @DisplayName("method -> deleteAll should verify ")
    void deleteAllTest() {
        //GIVEN
        //WHEN
        testDataService.deleteAll();
        //THEN
        verify(itemRepo).deleteAll();
    }
    @Test
    @DisplayName("method -> addTestData and expect testDataItemsNotFoundException")
    void addTestDataAndExpectTestDataItemsNotFoundException() throws IOException {
        //GIVEN
        when(serviceUtils.parseListFromJson(any(), any())).thenThrow(new IOException());


        assertThrows(TestDataItemsNotFoundException.class, () -> testDataService.addTestData());

    }

}
