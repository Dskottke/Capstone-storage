package capstone.storage.backend.drivingorders;

import capstone.storage.backend.ExceptionMessage;
import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.exceptions.IllegalTypeException;
import capstone.storage.backend.exceptions.ItemOrStorageBinNotExistingException;
import capstone.storage.backend.exceptions.NotEnoughItemsRemainingException;
import capstone.storage.backend.exceptions.StorageBinFalseItemException;
import capstone.storage.backend.item.ItemService;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.storagebin.StorageBinService;
import capstone.storage.backend.storagebin.models.StorageBin;
import capstone.storage.backend.utils.ServiceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertFalse;
import static com.mongodb.assertions.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @DisplayName("Method -> should return the expected List")
    void getAllDrivingOrdersByTypeInputAndGetExpectedList() {
        //GIVEN
        List<DrivingOrder> expectedList = List.of(new DrivingOrder("1", "2", 1, Type.INPUT, 20));
        //WHEN
        when(drivingOrderRepo.findByType(Type.INPUT)).thenReturn(expectedList);
        List<DrivingOrder> actual = drivingOrderService.getAllDrivingOrdersByType(Type.INPUT);
        //THEN
        assertEquals(expectedList, actual);
    }


    @Test
    @DisplayName("method -> isNullOrEmpty should return true because one field is null")
    void isNullOrEmptyWithOneFieldNullShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 2, 0);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return true because two fields are null")
    void isNullOrEmptyWithTwoFieldsNullShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 0, 0);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> isNullOrEmpty should return true because all fields are null")
    void isNullOrEmptyWithAllFieldsNullShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder(null, 0, 0);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("method -> isNullOrEmpty should return true because itemNumber and amount are empty")
    void isNullOrEmptyWithItemNumberAndAmountEmptyShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 0, 0);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return true because storageLocationId is empty")
    void isNullOrEmptyWitStorageLocationIdFieldIsEmptyShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("", 1, 1);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return true because storageLocationId and itemNumber are empty")
    void isNullOrEmptyWitStorageLocationIdFieldAndItemNumberAreEmptyShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("", 0, 1);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return true because itemNumber field is empty")
    void isNullOrEmptyWithItemNumberEmptyShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 0, 1);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return true because amount field is empty")
    void isNullOrEmptyWithAmountEmptyShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 1, 0);
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
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("", 0, 0);
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
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 2, 3);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = false;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> isNullOrEmpty should return true because one field is null and one field is empty")
    void isNullOrEmptyWithOneFieldNullAndOneFieldEmptyStringShouldReturnTrue() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder(null, 2, 0);
        //WHEN
        boolean actual = drivingOrderService.isNullOrEmpty(newDrivingOrder);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("method -> getTotalAmountFromList and return 10")
    void getTotalAmountFromList() {
        //GIVEN
        List<DrivingOrder> testInputDrivingOrders = List.of(
                new DrivingOrder("1", "1", 1, Type.INPUT, 5),
                new DrivingOrder("2", "1", 1, Type.INPUT, 5));
        //WHEN
        int actual = drivingOrderService.getTotalAmountFromList(testInputDrivingOrders);
        int expected = 10;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method -> checkValidation should return false because there is an existing DrivingOrder with not matching item on the storage bin ")
    void checkInputValidationWithExistingDrivingOrderNotMatchingItemOnStorageBinShouldReturnFalse() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 1, 20);
        Item testItem = new Item("1", "test", "test", "test", "1", 30, 1, 1);
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 2, Type.INPUT, 5);
        //WHEN
        when(drivingOrderRepo.findFirstByStorageLocationIdAndType(testStorageBin.locationId(), Type.INPUT)).thenReturn(Optional.of(testDrivingOrder));
        boolean actual = drivingOrderService.isValidInputItem(testStorageBin, testItem.itemNumber());

        //THEN
        assertFalse(actual);
    }

    @Test
    @DisplayName("method -> checkValidation should return true because StorageBin-itemNumber equals item-itemNumber ")
    void checkInputValidationShouldReturnTrue() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 1, 20);
        Item testItem = new Item("1", "test", "test", "test", "1", 30, 1, 1);
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.INPUT, 5);
        //WHEN
        when(drivingOrderRepo.findFirstByStorageLocationIdAndType(testStorageBin.locationId(), Type.INPUT)).thenReturn(Optional.of(testDrivingOrder));
        boolean actual = drivingOrderService.isValidInputItem(testStorageBin, testItem.itemNumber());
        //THEN
        assertTrue(actual);
    }

    @Test
    @DisplayName("method -> INPUT isValidInput should return false because StorageBin-itemNumber is different to Item-ItemNumber ")
    void checkInputValidationShouldReturnFalseBecauseStorageBinItemNumberAndItemItemNumberAreDifferent() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 1, 20);
        Item testItem = new Item("1", "test", "test", "test", "1", 30, 2, 1);
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.INPUT, 5);
        //WHEN
        when(drivingOrderRepo.findFirstByStorageLocationIdAndType(testStorageBin.locationId(), Type.INPUT)).thenReturn(Optional.of(testDrivingOrder));
        boolean actual = drivingOrderService.isValidInputItem(testStorageBin, testItem.itemNumber());
        //THEN
        assertFalse(actual);
    }

    @Test
    @DisplayName("method -> INPUT isValidInput should return true because StorageBin-itemNumber is 0 ")
    void checkInputValidationShouldReturnTrueBecauseStorageBinItemNumberIs0() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 0, 20);
        Item testItem = new Item("1", "test", "test", "test", "1", 30, 2, 1);
        //WHEN
        when(drivingOrderRepo.findFirstByStorageLocationIdAndType(testStorageBin.locationId(), Type.INPUT)).thenReturn(Optional.empty());
        boolean actual = drivingOrderService.isValidInputItem(testStorageBin, testItem.itemNumber());
        //THEN
        assertTrue(actual);
    }

    @Test
    @DisplayName("method -> INPUT checkValidation should return true because StorageBin-itemNumber is 12 ")
    void checkInputValidationInputShouldReturnFalseBecauseStorageBinItemNumberIs12() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 12, 20);
        Item testItem = new Item("1", "test", "test", "test", "1", 30, 2, 1);
        //WHEN
        when(drivingOrderRepo.findFirstByStorageLocationIdAndType(testStorageBin.locationId(), Type.INPUT)).thenReturn(Optional.empty());
        boolean actual = drivingOrderService.isValidInputItem(testStorageBin, testItem.itemNumber());
        //THEN
        assertFalse(actual);
    }

    @Test
    @DisplayName("method -> addNewInputDrivingOrder should throw StorageBinFalseItemException ")
    void addNewInputDrivingOrderShouldThrowException() {
        //GIVEN
        Item testItem = new Item("1", "test", "test", "ger", "test", 10, 1, 1);
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 1, 1);
        StorageBin testStorageBin = new StorageBin("1", "1", 0, 1);
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 2, Type.INPUT, 5);

        //WHEN
        when(itemService.existByItemNumber(newDrivingOrder.itemNumber())).thenReturn(true);
        when(storageBinService.existsByLocationId(newDrivingOrder.storageLocationId())).thenReturn(true);
        when(itemService.findItemByItemNumber(newDrivingOrder.itemNumber())).thenReturn(testItem);
        when(storageBinService.findStorageBinByLocationId(newDrivingOrder.storageLocationId())).thenReturn(testStorageBin);
        when(drivingOrderRepo.findFirstByStorageLocationIdAndType(testStorageBin.locationId(), Type.INPUT)).thenReturn(Optional.of(testDrivingOrder));
        try {
            drivingOrderService.addNewDrivingOrder(Type.INPUT, newDrivingOrder);
            Assertions.fail();
        }
        //THEN
        catch (StorageBinFalseItemException e) {
            assertEquals(ExceptionMessage.STORAGE_BIN_FALSE_ITEM_EXCEPTION_MESSAGE.toString(), e.getMessage());
        }
    }

    @Test
    @DisplayName("method -> OUTPUT checkValidation should return false because the ItemNumber of testStorageBin and testItem don't match")
    void checkOutputValidationInputShouldReturnFalseBecauseStorageBinItemNumberIs12() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 12, 20);
        Item testItem = new Item("1", "test", "test", "test", "1", 30, 2, 1);
        //WHEN
        boolean actual = drivingOrderService.checkOutputValidation(testStorageBin, testItem);
        //THEN
        assertFalse(actual);
    }

    @Test
    @DisplayName("method -> OUTPUT checkValidation should return true because itemNumber of storageBinTest and testItem are equal ")
    void checkOutputValidationInputShouldReturnTrue() {
        //GIVEN
        StorageBin testStorageBin = new StorageBin("1", "1", 2, 20);
        Item testItem = new Item("1", "test", "test", "test", "1", 30, 2, 1);
        //WHEN
        boolean actual = drivingOrderService.checkOutputValidation(testStorageBin, testItem);
        //THEN
        assertTrue(actual);
    }

    @Test
    @DisplayName("method-> beforeDoneControl should throw NotEnoughItemsRemainingException ")
    void beforeDoneControlShouldThrowException() {
        //GIVEN
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.OUTPUT, 10);
        StorageBin mockStorageBin = new StorageBin("1", "1", 1, 0);
        //WHEN
        when(storageBinService.findStorageBinByLocationId(testDrivingOrder.storageLocationId())).thenReturn(mockStorageBin);
        try {
            drivingOrderService.beforeDoneControl(testDrivingOrder);
            fail();
        }
        //THEN
        catch (NotEnoughItemsRemainingException e) {
            assertEquals(ExceptionMessage.NOT_ENOUGH_ITEMS_REMAINING_EXCEPTION_MESSAGE.toString(), e.getMessage());
        }

    }

    @Test
    @DisplayName("method-> beforeDoneControl should return true because the storageBin will be empty ")
    void beforeDoneControlShouldReturnTrue() {
        //WHEN
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.OUTPUT, 10);
        StorageBin mockStorageBin = new StorageBin("1", "1", 1, 10);
        //GIVEN
        when(storageBinService.findStorageBinByLocationId(testDrivingOrder.storageLocationId())).thenReturn(mockStorageBin);
        boolean actual = drivingOrderService.beforeDoneControl(testDrivingOrder);
        //THEN
        assertTrue(actual);
    }

    @Test
    @DisplayName("method-> beforeDoneControl should return false because the Storage Bin will not be empty ")
    void beforeDoneControlShouldReturnFalse() {
        //WHEN
        DrivingOrder testDrivingOrder = new DrivingOrder("1", "1", 1, Type.OUTPUT, 10);
        StorageBin mockStorageBin = new StorageBin("1", "1", 1, 12);
        //GIVEN
        when(storageBinService.findStorageBinByLocationId(testDrivingOrder.storageLocationId())).thenReturn(mockStorageBin);
        boolean actual = drivingOrderService.beforeDoneControl(testDrivingOrder);
        //THEN
        assertFalse(actual);
    }

    @Test
    @DisplayName("method -> addNewDrivingOrder should throw IllegalTypeException because the given type is null")
    void addNewDrivingOrderShouldThrowIllegalTypeException() {
        //GIVEN
        Item testItem = new Item("1", "test", "test", "ger", "test", 10, 1, 1);
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 1, 1);
        StorageBin testStorageBin = new StorageBin("1", "1", 0, 1);
        //WHEN
        when(itemService.existByItemNumber(newDrivingOrder.itemNumber())).thenReturn(true);
        when(storageBinService.existsByLocationId(newDrivingOrder.storageLocationId())).thenReturn(true);
        when(itemService.findItemByItemNumber(newDrivingOrder.itemNumber())).thenReturn(testItem);
        when(storageBinService.findStorageBinByLocationId(newDrivingOrder.storageLocationId())).thenReturn(testStorageBin);
        try {
            drivingOrderService.addNewDrivingOrder(null, newDrivingOrder);
            Assertions.fail();
        }
        //THEN
        catch (IllegalTypeException e) {
            assertEquals(ExceptionMessage.ILLEGAL_TYPE_EXCEPTION_MESSAGE.toString(), e.getMessage());
        }

    }

    @Test
    @DisplayName("method -> addNewDrivingOrder should throw ItemOrStorageBinNotExistingException because existsByItemNumber returns false")
    void addNewDrivingOrderWithFalseExistByItemNumberShouldThrowItemOrStorageBinNotExistingException() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 1, 1);
        //WHEN
        when(itemService.existByItemNumber(newDrivingOrder.itemNumber())).thenReturn(false);
        when(storageBinService.existsByLocationId(newDrivingOrder.storageLocationId())).thenReturn(true);
        try {
            drivingOrderService.addNewDrivingOrder(Type.INPUT, newDrivingOrder);
            Assertions.fail();
        }
        //THEN
        catch (ItemOrStorageBinNotExistingException e) {
            assertEquals(ExceptionMessage.ITEM_OR_STORAGE_BIN_NOT_EXISTING_EXCEPTION_MESSAGE.toString(), e.getMessage());
        }
    }

    @Test
    @DisplayName("method -> addNewDrivingOrder should throw ItemOrStorageBinNotExistingException because existsByLocationId returns false")
    void addNewDrivingOrderWithFalseExistsByLocationIdShouldThrowItemOrStorageBinNotExistingException() {
        //GIVEN
        NewDrivingOrder newDrivingOrder = new NewDrivingOrder("1", 1, 1);
        //WHEN
        when(itemService.existByItemNumber(newDrivingOrder.itemNumber())).thenReturn(true);
        when(storageBinService.existsByLocationId(newDrivingOrder.storageLocationId())).thenReturn(false);
        try {
            drivingOrderService.addNewDrivingOrder(Type.INPUT, newDrivingOrder);
            Assertions.fail();
        }
        //THEN
        catch (ItemOrStorageBinNotExistingException e) {
            assertEquals(ExceptionMessage.ITEM_OR_STORAGE_BIN_NOT_EXISTING_EXCEPTION_MESSAGE.toString(), e.getMessage());
        }
    }
}
