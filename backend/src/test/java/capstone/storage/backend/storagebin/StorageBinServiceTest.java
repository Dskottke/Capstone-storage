package capstone.storage.backend.storagebin;

import capstone.storage.backend.drivingorders.Type;
import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.exceptions.StorageBinNotFoundException;
import capstone.storage.backend.item.ItemRepo;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.models.StorageBin;
import capstone.storage.backend.storagebin.models.StorageBinReturn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class StorageBinServiceTest {

    private final StorageBinRepo storageBinrepo = mock(StorageBinRepo.class);
    private final ItemRepo itemRepo = mock(ItemRepo.class);
    private final StorageBinService service = new StorageBinService(storageBinrepo, itemRepo);

    @Test
    @DisplayName("Method -> getAllStorageBins -> should return the expected list.")
    void getAllStorageBinsAndReturnExpectedList() {
        //GIVEN
        List<StorageBinReturn> expectedList = List.of(new StorageBinReturn("1", "1", 1, 20, "test"));
        List<StorageBin> mockList = List.of(new StorageBin("1", "1", 1, 20));
        Item mockItem = new Item("1", "test", "test", "ger", "123", 40, 1, 0);
        //WHEN
        when(itemRepo.findItemByItemNumber(mockItem.itemNumber())).thenReturn(Optional.of(mockItem));
        when(storageBinrepo.findAll()).thenReturn(mockList);
        List<StorageBinReturn> actual = service.getAllStorageBins();
        //THEN
        verify(itemRepo).findItemByItemNumber(mockItem.itemNumber());
        verify(storageBinrepo).findAll();
        assertEquals(expectedList, actual);
    }

    @Test
    @DisplayName("Method -> addItemNameToStorageList should update the StorageBin in DB with the itemNumber matching ItemName.")
    void getAllStorageBinAndExpectStorageBinWithMatchingItemName() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 1, 2);
        List<StorageBin> testStorageBinList = List.of(testStorageBin);
        Item testItem = new Item("992901d9-5eb8-4992-9620-e5e80bb7f0e0", "Axe Bodyspray Wasabi & Fresh Linen", "Unknown", "NL", "8710847909610", 20, 1, 2);
        //WHEN
        StorageBin expected = new StorageBin("1", "1", 1, 2);
        when(storageBinrepo.findAll()).thenReturn(testStorageBinList);
        when(itemRepo.findItemByItemNumber(testStorageBin.itemNumber())).thenReturn(Optional.of(testItem));
        when(storageBinrepo.findStorageBinByLocationId("1")).thenReturn(expected);

        StorageBin actual = storageBinrepo.findStorageBinByLocationId("1");
        //THEN
        verify(storageBinrepo).findAll();
        verify(itemRepo).findItemByItemNumber(testStorageBin.itemNumber());
        verify(storageBinrepo).findStorageBinByLocationId("1");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> addItemNameToStorageList -> should return list with storageBins where storedItemName is empty String.")
    void getAllStorageBinsShouldReturnExpectedList() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 0, 0);
        List<StorageBin> testStorageBinList = List.of(testStorageBin);
        List<StorageBinReturn> expected = List.of(new StorageBinReturn("1", "1", 0, 0, ""));
        //WHEN
        when(storageBinrepo.findAll()).thenReturn(testStorageBinList);
        when(itemRepo.findItemByItemNumber(testStorageBin.itemNumber())).thenReturn(Optional.empty());
        List<StorageBinReturn> actual = service.getAllStorageBins();
        //THEN
        verify(storageBinrepo).findAll();
        verify(itemRepo).findItemByItemNumber(testStorageBin.itemNumber());
        assertEquals(expected, actual);
    }

    @DisplayName("Method -> updateOutputStorageBin -> should return StorageBin with empty storedItemName and itemNumber 0 .")
    @Test
    void updateOutputStorageBinAndExpectStorageBinWithEmptyItemNameAndItemNumber0() {
        //GIVEN
        boolean testStorageBinIsEmpty = false;
        StorageBin expected = new StorageBin("1", "1", 0, 0);
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.OUTPUT, 10);
        StorageBin testStorageBin = new StorageBin("1", "1", 1, 10);
        //WHEN
        when(storageBinrepo.findById(testDrivingOrder.storageLocationId())).thenReturn(Optional.of(testStorageBin));
        when(storageBinrepo.findStorageBinByLocationId(testStorageBin.locationId())).thenReturn(expected);
        service.updateOutputStorageBin(testStorageBinIsEmpty, testDrivingOrder);
        StorageBin actual = storageBinrepo.findStorageBinByLocationId(testStorageBin.locationId());
        //THEN
        verify(storageBinrepo).findById(testDrivingOrder.storageLocationId());
        verify(storageBinrepo).findStorageBinByLocationId(testStorageBin.locationId());
        assertEquals(expected, actual);
    }

    @DisplayName("method -> updateOutputStorageBin -> should return StorageBin with same ItemNumber but updated amount ")
    @Test
    void updateOutputStorageBinAndExpectStorageBinWithSameItemNumberAndNewAmount() {
        //GIVEN
        boolean testStorageBinIsEmpty = true;
        StorageBin expected = new StorageBin("1", "1", 0, 10);
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.OUTPUT, 10);
        StorageBin testStorageBin = new StorageBin("1", "1", 1, 20);
        //WHEN
        when(storageBinrepo.findById(testDrivingOrder.storageLocationId())).thenReturn(Optional.of(testStorageBin));
        when(storageBinrepo.findStorageBinByLocationId(testStorageBin.locationId())).thenReturn(expected);
        service.updateOutputStorageBin(testStorageBinIsEmpty, testDrivingOrder);
        StorageBin actual = storageBinrepo.findStorageBinByLocationId(testStorageBin.locationId());
        //THEN
        verify(storageBinrepo).findById(testDrivingOrder.storageLocationId());
        verify(storageBinrepo).findStorageBinByLocationId(testStorageBin.locationId());
        assertEquals(expected, actual);
    }

    @DisplayName("Method -> getAmountsFromStorageBins -> should Return 1.")
    @Test
    void getAmountsFromStorageBinsByItemNumberAndExpectReturn1() {
        //GIVEN
        Item itemToCount = new Item("1", "Axe Bodyspray Wasabi & Fresh Linen", "ger", "DE", "123", 10, 1, 0);
        List<StorageBin> storageBinsWithItem = List.of(new StorageBin("1", "1", 1, 1));
        //WHEN
        when(storageBinrepo.findAllByItemNumber(itemToCount.itemNumber())).thenReturn(storageBinsWithItem);
        int actual = service.getAmountsFromStorageBins(itemToCount);
        int expected = 1;
        //THEN
        verify(storageBinrepo).findAllByItemNumber(itemToCount.itemNumber());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> updateInputStorageBin -> should throw StorageBinNotFoundException because the storageBinRepo returns Optional empty.")
    void updateInputStorageBinShouldThrowStorageBinNotFoundException() {
        //GIVEN
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.INPUT, 10);
        //WHEN
        when(storageBinrepo.findById(testDrivingOrder.storageLocationId())).thenReturn(Optional.empty());
        try {
            service.updateInputStorageBin(testDrivingOrder);
            fail();
        } catch (StorageBinNotFoundException e) {
            //THEN
            verify(storageBinrepo).findById(testDrivingOrder.storageLocationId());
            assertEquals("Can't find storage-bin with ID: 1", e.getMessage());
        }
    }

    @Test
    @DisplayName("Method -> updateOutputStorageBin -> should throw StorageBinNotFoundException because the storageBinRepo returns Optional empty.")
    void updateOutputStorageBinShouldThrowStorageBinNotFoundException() {
        //GIVEN
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.OUTPUT, 10);
        //WHEN
        when(storageBinrepo.findById(testDrivingOrder.storageLocationId())).thenReturn(Optional.empty());
        try {
            service.updateOutputStorageBin(true, testDrivingOrder);
            fail();
        } catch (StorageBinNotFoundException e) {
            //THEN
            verify(storageBinrepo).findById(testDrivingOrder.storageLocationId());
            assertEquals("Can't find storage-bin with ID: 1", e.getMessage());
        }
    }
}
