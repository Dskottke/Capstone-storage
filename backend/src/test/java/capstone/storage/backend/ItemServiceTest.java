package capstone.storage.backend;

import capstone.storage.backend.utils.ItemUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {
    private final ItemRepo itemRepo = mock(ItemRepo.class);
    private final ItemUtils utils = mock(ItemUtils.class);
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


        when(eanApiService.getArticleResponse(eanToFind)).thenReturn(response);
        when(utils.generateUUID()).thenReturn("123");
        when(itemRepo.save(itemToExpect)).thenReturn(itemToExpect);

        //WHEN
        Item actual = itemService.addItem(eanToFind);
        Item expected = itemToExpect;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void saveItem() {
    }

    @Test
    void existById() {
    }

    @Test
    void deleteItemById() {
    }
}