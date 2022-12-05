package capstone.storage.backend.drivingorders;

import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.drivingorders.models.NewDrivingOrder;
import capstone.storage.backend.item.ItemService;
import capstone.storage.backend.storagebin.StorageBinService;
import capstone.storage.backend.utils.ServiceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
}