package capstone.storage.backend.item;

import capstone.storage.backend.drivingorders.DrivingOrderRepo;
import capstone.storage.backend.exceptions.*;
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
    @DisplayName("Method -> findAll -> should return an empty list.")
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
    @DisplayName("Method -> addItem -> should return the item to add")
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
                20,
                1
                , 0);

        AddItemDto addItemDto = new AddItemDto(eanToFind, 1, 20);
        when(eanApiService.getItemResponseFromApi(eanToFind)).thenReturn(response);
        when(utils.generateUUID()).thenReturn("123");
        when(itemRepo.insert(itemToExpect)).thenReturn(itemToExpect);
        //WHEN
        Item actual = itemService.addItem(addItemDto);
        Item expected = itemToExpect;
        //THEN
        verify(eanApiService).getItemResponseFromApi(eanToFind);
        verify(utils).generateUUID();
        verify(itemRepo).insert(itemToExpect);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> addItem -> should throw ItemValidationException because itemNumber is -5.")
    void addItemShouldThrowItemValidationExceptionBecauseItemNumberIsLess0() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto("123", -5, 20);
        //WHEN
        try {
            itemService.addItem(addItemDto);
            fail();
        } catch (ItemValidationException e) {
            //THEN
            String expected = "The capacity: 20 and the item-number: -5 must be greater than 0.";
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    @DisplayName("method -> addItem -> should throw ItemValidationException because storableValue is -5")
    void addItemShouldThrowItemValidationExceptionBecauseStorableValueIsLess0() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto("123", 1, -5);
        //WHEN
        try {
            itemService.addItem(addItemDto);
            fail();
        } catch (ItemValidationException e) {
            //THEN
            String expected = "The capacity: -5 and the item-number: 1 must be greater than 0.";
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    @DisplayName("method -> updateItem -> should return updated item.")
    void UpdateItemAndExpectSameItemToReturn() {
        //GIVEN
        Item itemToExpect = new Item("123",
                "testName",
                "testCategory",
                "GER", "8710847909610",
                20,
                1,
                1);
        when(itemRepo.save(itemToExpect)).thenReturn(itemToExpect);
        //WHEN
        Item actual = itemService.updateItem(itemToExpect);
        Item expected = itemRepo.save(itemToExpect);
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> existById -> should return true")
    void existById() {
        //GIVEN
        String id = "123";
        when(itemRepo.existsById(id)).thenReturn(true);
        //WHEN
        boolean actual = itemService.existById(id);
        boolean expected = true;
        //THEN
        verify(itemRepo).existsById(id);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> deleteItemById -> should give the itemToDelete to the itemRepo.")
    void deleteItemById() {
        //GIVEN
        Item itemToDelete = new Item("123",
                "testName",
                "testCategory",
                "GER", "8710847909610",
                20,
                1,
                0);
        //WHEN
        doNothing().when(itemRepo).deleteById(itemToDelete.id());
        when(itemRepo.findById(itemToDelete.id())).thenReturn(Optional.of(itemToDelete));
        itemService.deleteItemById(itemToDelete.id());
        //THEN
        verify(itemRepo).findById(itemToDelete.id());
        verify(itemRepo).deleteById(itemToDelete.id());
    }

    @Test
    @DisplayName("Method : checkItemExisting with itemRepo.existsByItemNumber true -> should throw ItemAlreadyExistingException.")
    void checkItemExistsThrowsItemAlreadyExistingException() {
        //GIVEN

        AddItemDto testAddItemDto = new AddItemDto("8710847909610", 123, 1);
        when(itemRepo.existsByItemNumber(testAddItemDto.itemNumber())).thenReturn(true);
        //WHEN
        try {
            itemService.checkItemExisting(testAddItemDto);
            fail();
        }
        //THEN
        catch (ItemAlreadyExistsException e) {
            String actual = e.getMessage();
            String expected = "The item with ean: 8710847909610 is already existing!";
            verify(itemRepo).existsByItemNumber(testAddItemDto.itemNumber());
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("Method -> checkItemExisting with itemRepo.existsByEan true -> should throw ItemAlreadyExistsException because the item already exists.")
    void checkIfItemAlreadyExistWithExistingEanReturnTrue() {
        //GIVEN

        AddItemDto testAddItemDto = new AddItemDto("8710847909610", 123, 1);

        //WHEN
        when(itemRepo.existsByEan(testAddItemDto.ean())).thenReturn(true);
        try {
            itemService.checkItemExisting(testAddItemDto);
            fail();
        }
        //THEN
        catch (ItemAlreadyExistsException e) {
            String actual = e.getMessage();
            String expected = "The item with ean: 8710847909610 is already existing!";
            verify(itemRepo).existsByEan(testAddItemDto.ean());
            assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("Method -> isNullOrEmpty -> should return true when all AddItemDto fields are null.")
    void checkIfIsNullOrEmptyReturnsTrueWhenAddITemDtoFieldsAreNullOr0() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto(null, 0, 0);
        //WHEN
        boolean actual = itemService.isNullOrEmpty(addItemDto);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> isNullOrEmpty -> should return true when all AddItemDto fields are empty.")
    void checkIfIsNullOrEmptyReturnsTrueWhenAddITemDtoStorableValueIsEmptyString() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto("", 0, 0);
        //WHEn
        boolean actual = itemService.isNullOrEmpty(addItemDto);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> isNullOrEmpty -> should return false when all AddItemDto fields are not empty.")
    void checkIfIsNullOrEmptyReturnsFalseWhenAddITemDtoFieldsAreNotEmptyString() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto("123", 1, 1);
        //WHEn
        boolean actual = itemService.isNullOrEmpty(addItemDto);
        boolean expected = false;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> isNullOrEmpty -> should return true when one AddItemDto field is empty.")
    void checkIfIsNullOrEmptyReturnsTrueWhen1AddITemDtoIsEmptyString() {
        //GIVEN
        AddItemDto addItemDto = new AddItemDto("", 1, 1);
        //WHEn
        boolean actual = itemService.isNullOrEmpty(addItemDto);
        boolean expected = true;
        //THEN
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Method -> beforeDeleteControl should return true.")
    void beforeDeleteControlShouldReturnTrueBecauseDrivingOrderRepoReturnsTrue() {
        //GIVEN
        String id = "1";
        Item item = new Item(
                "1",
                "test",
                "testCategory",
                "GER",
                "123",
                12,
                12,
                10);
        //WHEN
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(storageBinService.existsByItemNumber(item.itemNumber())).thenReturn(true);
        when(drivingOrderRepo.existsByItemNumber(item.itemNumber())).thenReturn(false);
        boolean actual = itemService.hasStock(id);
        //THEN
        verify(itemRepo).findById(id);
        verify(storageBinService).existsByItemNumber(item.itemNumber());
        verify(drivingOrderRepo).existsByItemNumber(item.itemNumber());
        assertTrue(actual);
    }

    @Test
    @DisplayName("method : beforeDeleteControl should return true because the storageBinService returns true and the drivingOrderRepo returns false")
    void beforeDeleteControlShouldReturnTrueBecauseStorageBinRepoReturnsTrue() {
        //GIVEN
        String id = "1";
        Item item = new Item(
                "1",
                "test",
                "testCategory",
                "GER",
                "123",
                12,
                12,
                10);
        //WHEN
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(storageBinService.existsByItemNumber(item.itemNumber())).thenReturn(false);
        when(drivingOrderRepo.existsByItemNumber(item.itemNumber())).thenReturn(true);
        boolean actual = itemService.hasStock(id);
        //THEN
        verify(itemRepo).findById(id);
        verify(storageBinService).existsByItemNumber(item.itemNumber());
        verify(drivingOrderRepo).existsByItemNumber(item.itemNumber());
        assertTrue(actual);
    }

    @Test
    @DisplayName("method : beforeDeleteControl should return false because the storageBinService and drivingOrderRepo return false")
    void beforeDeleteControlShouldReturnFalseBecauseBothBooleanAreFalse() {
        //GIVEN
        String id = "1";
        Item item = new Item(
                "1",
                "test",
                "testCategory",
                "GER",
                "123",
                12,
                12,
                10);
        //WHEN
        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(storageBinService.existsByItemNumber(item.itemNumber())).thenReturn(false);
        when(drivingOrderRepo.existsByItemNumber(item.itemNumber())).thenReturn(false);
        boolean actual = itemService.hasStock(id);
        //THEN
        verify(itemRepo).findById(id);
        verify(storageBinService).existsByItemNumber(item.itemNumber());
        verify(drivingOrderRepo).existsByItemNumber(item.itemNumber());
        assertFalse(actual);
    }

    @DisplayName("Method -> deleteItemById -> should throw StoredItemException because the storageBinService and drivingOrderRepo return true")
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
                12,
                12,
                10);
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
            verify(itemRepo).findById(id);
            verify(storageBinService).existsByItemNumber(item.itemNumber());
            verify(drivingOrderRepo).existsByItemNumber(item.itemNumber());
            assertEquals("Can't delete item with ID: 1 because there are open driving-orders or items are still stored", e.getMessage());
        }
    }

    @DisplayName("Method -> deleteItemById -> should throw ItemNotFoundException because the itemRepo returns Optional.empty.")
    @Test
    void deleteItemByIdShouldThrowItemNotFoundException() {
        //GIVEN
        String id = "1";
        //WHEN
        when(itemRepo.findById(id)).thenReturn(Optional.empty());

        try {
            itemService.deleteItemById(id);
            fail();
        }
        //THEN
        catch (ItemNotFoundException e) {
            verify(itemRepo).findById(id);
            assertEquals("Can't find item", e.getMessage());
        }
    }


    @Test
    @DisplayName("Method -> should throw ItemNotExistingException because the repository returns null.")
    void findItemByItemNumberThrowItemIsNotExistingException() {
        //GIVEN
        int itemNumber = 1;
        //WHEN
        when(itemRepo.findItemByItemNumber(itemNumber)).thenReturn(Optional.empty());
        try {
            itemService.findItemByItemNumber(itemNumber);
            fail();
        }
        //THEN
        catch (ItemISNotExistingException e) {
            verify(itemRepo).findItemByItemNumber(itemNumber);
            assertEquals("Item is not existing!", e.getMessage());
        }

    }

}
