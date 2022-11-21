package capstone.storage.backend;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {
    private final ItemRepo itemRepo = mock(ItemRepo.class);

    @Test
    void findAllItemsAndExpectEmptyList() {
        //GIVEN
        List<Item> itemList = new ArrayList<>();
        when(itemRepo.findAll()).thenReturn(itemList);
        //WHEN
        List<Item> actual = itemRepo.findAll();
        List<Item> expected = itemList;
        //THEN
        verify(itemRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    void getItemResponse() {
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