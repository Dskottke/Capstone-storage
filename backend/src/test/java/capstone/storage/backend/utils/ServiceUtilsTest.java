package capstone.storage.backend.utils;

import capstone.storage.backend.drivingorders.Type;
import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.testdata.ResourcePath;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceUtilsTest {

    private final ServiceUtils serviceUtils = new ServiceUtils();

    @Test
    @DisplayName("Method -> parseListFromJson should return parsed list of driving-orders.")
    void parseListFromJsonShouldReturnExpectedList() throws IOException {
        //GIVEN
        String path = ResourcePath.DRIVING_ORDER_PATH.toString();
        List<DrivingOrder> expected = List.of(
                new DrivingOrder(
                        "647cc8fd-c81c-47ac-a9bb-fd1a7ff0e288",
                        "100",
                        10,
                        Type.INPUT,
                        10));
        //WHEN
        List<DrivingOrder> actual = serviceUtils.parseListFromJson(new TypeReference<>() {
        }, path);
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> generateUUID -> the returned UUID should have matching REGEX pattern and the invalid-uuid not.")
    void generateUUID() {
        //GIVEN
        Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        //THEN
        Assertions.assertTrue(UUID_REGEX.matcher(serviceUtils.generateUUID()).matches());
        Assertions.assertFalse(UUID_REGEX.matcher("invalid-uuid").matches());

    }
}
