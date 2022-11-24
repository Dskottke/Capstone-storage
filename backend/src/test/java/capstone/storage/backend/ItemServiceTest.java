package capstone.storage.backend;

import capstone.storage.backend.utils.ServiceUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {
    private final ItemRepo itemRepo = mock(ItemRepo.class);
    private final ServiceUtils utils = mock(ServiceUtils.class);
    private final EanApiService eanApiService = mock(EanApiService.class);

    private final ItemService itemService = new ItemService(itemRepo, eanApiService, utils);

    @Test
    void findAllItemsAndExpectEmptyList() {
        //GIVEN
        List<Item> itemList = new ArrayList<>();
        when(itemRepo.findAll()).thenReturn(itemList);
        //WHEN
        List<Item> actual = itemService.findAll();
        List<Item> expected = itemList;
        //THEN
        verify(itemRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void addItemByEanAndReturnItemWithId() {
        //GIVEN
        String eanToFind = "8710847909610";
        ItemResponse response = new ItemResponse(
                "testName", eanToFind,
                "testCategory",
                "GER");
        Item itemToExpect = new Item("123",
                "testName",
                "testCategory",
                "GER", eanToFind,
                "20");

        AddItemDto addItemDto = new AddItemDto(eanToFind, "1");
        when(eanApiService.getItemResponseFromApi(eanToFind)).thenReturn(response);
        when(utils.generateUUID()).thenReturn("123");
        when(itemRepo.insert(itemToExpect)).thenReturn(itemToExpect);

        //WHEN
        Item actual = itemService.addItem(addItemDto, eanToFind);
        Item expected = itemToExpect;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void UpdateItemAndExpectSameItemToReturn() {
        //GIVEN
        Item itemToExpect = new Item("123",
                "testName",
                "testCategory",
                "GER", "8710847909610",
                "20");
        when(itemRepo.save(itemToExpect)).thenReturn(itemToExpect);
        //WHEN
        Item actual = itemService.updateItem(itemToExpect);
        Item expected = itemRepo.save(itemToExpect);
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void existById() {
        //GIVEN
        String id = "123";
        when(itemRepo.existsById(id)).thenReturn(true);
        //WHEN
        boolean actual = itemService.existById(id);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void deleteItemById() {
        //GIVEN
        Item itemToDelete = new Item("123",
                "testName",
                "testCategory",
                "GER", "8710847909610",
                "20");
        //WHEN
        doNothing().when(itemRepo).deleteById(itemToDelete.id());
        itemService.deleteItemById(itemToDelete.id());

        //THEN
        verify(itemRepo).deleteById(itemToDelete.id());
    }
}
