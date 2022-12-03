package capstone.storage.backend.drivingorders;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DrivingOrderServiceTest {

    private final DrivingOrderRepo drivingOrderRepo = mock(DrivingOrderRepo.class);

    private final DrivingOrderService drivingOrderService = new DrivingOrderService(drivingOrderRepo);


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
}