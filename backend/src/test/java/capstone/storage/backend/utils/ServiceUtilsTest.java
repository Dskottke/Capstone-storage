package capstone.storage.backend.utils;

import capstone.storage.backend.item.models.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ServiceUtilsTest {
    private final ServiceUtils utils = new ServiceUtils();
    @Test
    @DisplayName("method : getListFromItemData() ->  should return expected List")
    void CheckIfTheItemFromDataIsMatching() throws IOException {
        //GIVEN
        List<Item> expected = List.of(new Item(
                "992901d9-5eb8-4992-9620-e5e80bb7f0e0",
                "Axe Bodyspray Wasabi & Fresh Linen",
                "Unknown",
                "NL",
                "8710847909610",
                "20",
                "1"));
        //WHEN
        List<Item> actual = utils.getListFromJson(new TypeReference<List<Item>>() {
        });
        //THEN
        assertEquals(expected, actual);
    }
}
