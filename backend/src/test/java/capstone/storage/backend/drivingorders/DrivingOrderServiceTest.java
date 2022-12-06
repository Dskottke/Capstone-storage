package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.exceptions.ExceptionMessage;
import capstone.storage.backend.exceptions.StorageBinFalseItemException;
import capstone.storage.backend.item.ItemService;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.StorageBin;
import capstone.storage.backend.storagebin.StorageBinService;
import capstone.storage.backend.utils.ServiceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DrivingOrderServiceTest {

    private final DrivingOrderRepo drivingOrderRepo = mock(DrivingOrderRepo.class);
    private final ServiceUtils serviceUtils = mock(ServiceUtils.class);
    private final ItemService itemService = mock(ItemService.class);
    private final StorageBinService storageBinService = mock(StorageBinService.class);
    private final DrivingOrderService drivingOrderService = new DrivingOrderService(
            drivingOrderRepo, itemService, serviceUtils, storageBinService);


    @Test
    @DisplayName("Mehtod -> should return the expected List")
    void getAllDrivingOrdersByTypeInputAndGetExpectedList() {
        //GIVEN
        List<DrivingOrder> expectedList = List.of(new DrivingOrder("1", "2", "1", Type.INPUT, "20"));
        //WHEN
        when(drivingOrderRepo.findByType(Type.INPUT)).thenReturn(expectedList);
        List<DrivingOrder> actual = drivingOrderService.getAllDrivingOrdersByType(Type.INPUT);
        //THEN
        assertEquals(expectedList, actual);
    }

    @Test
    @DisplayName("Method -> isNullOrEmpty should return true because all fields are null")
    void isNullOrEmptyWithAllFieldsNullShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder(null, null, null);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> isNullOrEmpty should return true because all fields are empty")
    void isNullOrEmptyWithAllFieldsAreEmptyShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("", "", "");
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return true because one field is empty")
    void isNullOrEmptyWithOneFieldIsEmptyShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", "1", "");
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return true because one field is null")
    void isNullOrEmptyWithOneFieldNullShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", "2", null);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return false because all fields are filled")
    void isNullOrEmptyWithOneFieldNullShouldReturnFalse() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", "2", "3");
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = false;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> itemAndStorageBinExisting should return true")
    void itemAndStorageBinExistingReturnsTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", "1", "1");
        //WHEN
        when(itemService.existByItemNumber(newDrivingOrder.itemNumber())).thenReturn(true);
        when(storageBinService.existsByLocationId(newDrivingOrder.storageLocationId())).thenReturn(true);
        boolean actual = drivingOrderService.itemAndStorageBinExisting(newDrivingOrder);
        //THEN
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> itemAndStorageBinExisting should return false")
    void itemAndStorageBinExistingReturnsFalse() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", "1", "1");
        //WHEN
        when(itemService.existByItemNumber(newDrivingOrder.itemNumber())).thenReturn(false);
        when(storageBinService.existsByLocationId(newDrivingOrder.storageLocationId())).thenReturn(false);
        boolean actual = drivingOrderService.itemAndStorageBinExisting(newDrivingOrder);
        //THEN
        boolean expected = false;
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("method -> getTotalAmountFromList and return 10")
    void getTotalAmountFromList() {
        //GIVEN
        List<DrivingOrder> testInputDrivingOrders = List.of(
                new DrivingOrder("1", "1", "1", Type.INPUT, "5"),
                new DrivingOrder("2", "1", "1", Type.INPUT, "5"));
        //WHEN
        int actual = drivingOrderService.getTotalAmountFromList(testInputDrivingOrders);
        int expected = 10;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> checkValidation should return false because there is an existing DrivingOrder with not matching item on the storage bin ")
    void checkValidationShouldReturnFalse() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", "1", "20");
        Item testItem = new Item("1", "test", "test", "test", "1", "30", "1");
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", "2", Type.INPUT, "5");
        //WHEN
        when(drivingOrderRepo.findFirstByStorageLocationId(testStorageBin.locationId())).thenReturn(Optional.of(testDrivingOrder));
        boolean actual = drivingOrderService.checkValidation(testStorageBin, testItem);
        boolean expected = false;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> addNewInputDrivingOrder should throw StorageBinFalseItemException ")
    void addNewInputDrivingOrderShouldThrowException() {
        //GIVEN
        Item testItem = new Item("1", "test", "test", "ger", "test", "10", "1");
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", "1", "1");
        StorageBin testStorageBin = new StorageBin("1", "1", "0", "1");
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", "2", Type.INPUT, "5");

        //WHEN
        when(itemService.existByItemNumber(newDrivingOrder.itemNumber())).thenReturn(true);
        when(storageBinService.existsByLocationId(newDrivingOrder.storageLocationId())).thenReturn(true);
        when(itemService.findItemByItemNumber(newDrivingOrder.itemNumber())).thenReturn(testItem);
        when(storageBinService.findStorageBinByLocationId(newDrivingOrder.storageLocationId())).thenReturn(testStorageBin);
        when(drivingOrderRepo.findFirstByStorageLocationId(testStorageBin.locationId())).thenReturn(Optional.of(testDrivingOrder));
        try {
            drivingOrderService.addNewInputDrivingOrder(newDrivingOrder);
            fail();
        }
        //THEN
        catch (StorageBinFalseItemException e) {
            assertEquals(ExceptionMessage.STORAGE_BIN_FALSE_ITEM_EXCEPTION_MESSAGE.toString(), e.getMessage());
        }

    }
}