package capstone.storage.backend.item;
import capstone.storage.backend.drivingorders.DrivingOrderRepo;
import capstone.storage.backend.exceptions.ExceptionMessage;
import capstone.storage.backend.exceptions.ItemAlreadyExistException;
import capstone.storage.backend.exceptions.ItemISNotExistingException;
import capstone.storage.backend.exceptions.StoredItemsException;
import capstone.storage.backend.item.models.AddItemDto;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.item.models.Product;
import capstone.storage.backend.storagebin.StorageBinService;
import capstone.storage.backend.utils.ServiceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {
    private final ItemRepo itemRepo = mock(ItemRepo.class);
    private final ServiceUtils utils = mock(ServiceUtils.class);
    private final ItemEanApiService eanApiService = mock(ItemEanApiService.class);
    private final StorageBinService storageBinService = mock(StorageBinService.class);
    private final DrivingOrderRepo drivingOrderRepo = mock(DrivingOrderRepo.class);
    private final ItemService itemService = new ItemService(itemRepo, eanApiService, storageBinService, drivingOrderRepo, utils);

    @Test
    @DisplayName("method : findAll -> should return an empty list")
    void findAllItemsAndExpectEmptyList() {
        //GIVEN
        List<Item> itemList = Collections.emptyList();
        when(itemRepo.findAll()).thenReturn(itemList);
        //WHEN
        List<Item> actual = itemService.findAll();
        List<Item> expected = itemList;
        //THEN
        verify(itemRepo).findAll();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method : addItem -> should return the item to add")
    void addItemByEanAndAddItemDtoAndReturnItemWithId() {
        //GIVEN
        String eanToFind = "8710847909610";
        Product response = new Product(
                "testName", eanToFind,
                "testCategory",
                "GER");
        Item itemToExpect = new Item("123",
                "testName",
                "testCategory",
                "GER", eanToFind,
                "20",
                "1"
                , "0");

        AddItemDto addItemDto = new AddItemDto(eanToFind, "1", "20");
        when(eanApiService.getItemResponseFromApi(eanToFind)).thenReturn(response);
        when(utils.generateUUID()).thenReturn("123");
        when(itemRepo.insert(itemToExpect)).thenReturn(itemToExpect);
        //WHEN
        Item actual = itemService.addItem(addItemDto);
        Item expected = itemToExpect;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method : updateItem -> should return updated item")
    void UpdateItemAndExpectSameItemToReturn() {
        //GIVEN
        Item itemToExpect = new Item("123",
                "testName",
                "testCategory",
                "GER", "8710847909610",
                "20",
                "1",
                "1");
        when(itemRepo.save(itemToExpect)).thenReturn(itemToExpect);
        //WHEN
        Item actual = itemService.updateItem(itemToExpect);
        Item expected = itemRepo.save(itemToExpect);
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method: existById -> should return true")
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
    @DisplayName("method: deleteItemById -> should give the itemToDelete to the itemRepo")
    void deleteItemById() {
        //GIVEN
        Item itemToDelete = new Item("123",
                "testName",
                "testCategory",
                "GER", "8710847909610",
                "20",
                "1",
                "0");
        //WHEN
        doNothing().when(itemRepo).deleteById(itemToDelete.id());
        when(itemRepo.findById(itemToDelete.id())).thenReturn(Optional.of(itemToDelete));
        itemService.deleteItemById(itemToDelete.id());
        //THEN
        verify(itemRepo).deleteById(itemToDelete.id());
    }

    @Test
    @DisplayName("method : checkItemExisting -> should throw ItemAlreadyExistingException")
    void ifItemIsAlreadyExistingThrowItemAlreadyExistingException() {
        //GIVEN
        String testEanToFind = "123";
        AddItemDto testAddItemDto = new AddItemDto("8710847909610", testEanToFind, "1");
        when(itemRepo.existsByItemNumber(testAddItemDto.itemNumber())).thenReturn(true);
        //WHEN
        try {
            itemService.checkItemExisting(testAddItemDto);
            fail();
        }
        //THEN
        catch (ItemAlreadyExistException e) {
            String actual = e.getMessage();
            String expected = ExceptionMessage.ITEM_ALREADY_EXIST_EXCEPTION_MESSAGE.toString();

            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("method : checkItemExisting -> should return true")
    void checkIfItemAlreadyExistWithExistingEanReturnTrue() {
        //GIVEN
        String testEanToFind = "123";
        AddItemDto testAddItemDto = new AddItemDto("8710847909610", testEanToFind, "1");

        //WHEN
        when(itemRepo.existsByEan(testEanToFind)).thenReturn(true);
        try {
            itemService.checkItemExisting(testAddItemDto);
            fail();
        }
        //THEN
        catch (ItemAlreadyExistException e) {
            String actual = e.getMessage();
            String expected = ExceptionMessage.ITEM_ALREADY_EXIST_EXCEPTION_MESSAGE.toString();

            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("method : isNullOrEmpty -> should return true when all AddItemDto fields are null")
    void checkIfIsNullOrEmptyReturnsTrueWhenAddITemDtoFieldsAreNull() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto(null, null, null);
        //WHEN
        boolean actual = itemService.isNullOrEmpty(addItemDto);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method : isNullOrEmpty -> should return true when all AddItemDto fields are empty")
    void checkIfIsNullOrEmptyReturnsTrueWhenAddITemDtoStorableValueIsEmptyString() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto("", "", "");
        //WHEn
        boolean actual = itemService.isNullOrEmpty(addItemDto);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method : isNullOrEmpty -> should return false when all AddItemDto fields are not empty")
    void checkIfIsNullOrEmptyReturnsFalseWhenAddITemDtoFieldsAreNotEmptyString() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto("123", "1", "1");
        //WHEn
        boolean actual = itemService.isNullOrEmpty(addItemDto);
        boolean expected = false;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method : isNullOrEmpty -> should return true when one AddItemDto field is empty ")
    void checkIfIsNullOrEmptyReturnsTrueWhen1AddITemDtoIsEmptyString() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto("", "1", "1");
        //WHEn
        boolean actual = itemService.isNullOrEmpty(addItemDto);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("method : beforeDeleteControl should return true")
    void beforeDeleteControlShouldReturnTrueBecauseDrivingOrderRepoReturnsTrue() {
        //GIVEN
        String id = "1";
        Item item = new Item(
                "1",
                "test",
                "testCategory",
                "GER",
                "123",
                "12",
                "12",
                "10");
        //WHEN
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(storageBinService.existsByItemNumber(item.itemNumber())).thenReturn(true);
        when(drivingOrderRepo.existsByItemNumber(item.itemNumber())).thenReturn(false);
        boolean actual = itemService.beforeDeleteControl(id);
        //THEN
        assertTrue(actual);
    }

    @Test
    @DisplayName("method : beforeDeleteControl should return true")
    void beforeDeleteControlShouldReturnTrueBecauseStorageBinRepoReturnsTrue() {
        //GIVEN
        String id = "1";
        Item item = new Item(
                "1",
                "test",
                "testCategory",
                "GER",
                "123",
                "12",
                "12",
                "10");
        //WHEN
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(storageBinService.existsByItemNumber(item.itemNumber())).thenReturn(false);
        when(drivingOrderRepo.existsByItemNumber(item.itemNumber())).thenReturn(true);
        boolean actual = itemService.beforeDeleteControl(id);
        //THEN
        assertTrue(actual);
    }
    @Test
    @DisplayName("method : beforeDeleteControl should return false")
    void beforeDeleteControlShouldReturnFalseBecauseBothBooleanAreFalse() {
        //GIVEN
        String id = "1";
        Item item = new Item(
                "1",
                "test",
                "testCategory",
                "GER",
                "123",
                "12",
                "12",
                "10");
        //WHEN
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(storageBinService.existsByItemNumber(item.itemNumber())).thenReturn(false);
        when(drivingOrderRepo.existsByItemNumber(item.itemNumber())).thenReturn(false);
        boolean actual = itemService.beforeDeleteControl(id);
        //THEN
        assertFalse(actual);
    }



    @DisplayName("method -> deleteItemById should throw StoredItemException")
    @Test
    void deleteItemByIdShouldThrowStoredItemException() {
        //GIVEN
        String id = "1";
        Item item = new Item(
                "1",
                "test",
                "testCategory",
                "GER",
                "123",
                "12",
                "12",
                "10");
        //WHEN
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(storageBinService.existsByItemNumber(item.itemNumber())).thenReturn(true);
        when(drivingOrderRepo.existsByItemNumber(item.itemNumber())).thenReturn(true);
        try {
            itemService.deleteItemById(id);
            fail();
        }
        //THEN
        catch (StoredItemsException e) {
            assertEquals(ExceptionMessage.STORED_ITEMS_EXCEPTION.toString(), e.getMessage());
        }
    }

    @Test
    @DisplayName("method -> should throw ItemNotExistingException because the repository returns null")
    void findItemByItemNumberThrowItemIsNotExistingException() {
        //GIVEN
        String itemNumber = "1";
        //WHEN
        when(itemRepo.findItemByItemNumber(itemNumber)).thenReturn(Optional.empty());
        try {
            itemService.findItemByItemNumber(itemNumber);
            fail();
        }
        //THEN
        catch (ItemISNotExistingException e) {
            assertEquals(ExceptionMessage.ITEM_IS_NOT_EXISTING.toString(), e.getMessage());
        }

    }
}
