package capstone.storage.backend.item;

import capstone.storage.backend.drivingorders.DrivingOrderRepo;
import capstone.storage.backend.exceptions.*;
import capstone.storage.backend.item.models.AddItemDto;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.item.models.Product;
import capstone.storage.backend.storagebin.StorageBinService;
import capstone.storage.backend.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepo repository;
    private final ItemEanApiService eanApiService;
    private final StorageBinService storageBinService;
    private final DrivingOrderRepo drivingOrderRepo;
    private final ServiceUtils utils;

    /**
     * This method returns the List of all items with the sum of amounts on storageBins for each item.
     *
     * @return List of Items with sum of amounts.
     */
    public List<Item> findAll() {
        List<Item> allItems = repository.findAll();
        List<Item> listToReturn = new ArrayList<>();

        for (Item item : allItems) {
            int amount = storageBinService.getAmountsFromStorageBins(item);

            Item itemToAdd = new Item(
                    item.id(),
                    item.name(),
                    item.categoryName(),
                    item.issuingCountry(),
                    item.ean(),
                    item.storableValue(),
                    item.itemNumber(),
                    amount);
            listToReturn.add(itemToAdd);
        }

        return listToReturn;

    }

    /**
     * This Method takes care that the addItem will be validated and gets proved if the item is already existing.
     * if everything is proved successfully it fetches the product from eanApiService by EAN and insert a new item into the
     * database with a default amount of 0.
     *
     * @param addItemDto to add a new item
     * @return Item
     */
    public Item addItem(AddItemDto addItemDto) {

        validateAddItemDto(addItemDto);
        checkItemExisting(addItemDto);

        Product product = eanApiService.getItemResponseFromApi(addItemDto.ean());


        int defaultStoringBinAmount = 0;
        Item itemToAdd = new Item(
                utils.generateUUID(),
                product.name(),
                product.categoryName(),
                product.issuingCountry(),
                product.ean(),
                addItemDto.storableValue(),
                addItemDto.itemNumber(),
                defaultStoringBinAmount);

        return repository.insert(itemToAdd);
    }


    public Item updateItem(Item articleRequest) {
        return repository.save(articleRequest);
    }

    public Item findItemByItemNumber(int itemNumber) {
        return repository.findItemByItemNumber(itemNumber).orElseThrow(ItemISNotExistingException::new);
    }

    public boolean existById(String id) {
        return repository.existsById(id);
    }

    public boolean existByItemNumber(int itemNumber) {
        return repository.existsByItemNumber(itemNumber);
    }

    /**
     * This method throws new StoredItemsException when the hasStock by id method returns true<br>
     * otherwise it deletes the item in database
     *
     * @param id of the item
     */
    public void deleteItemById(String id) {
        if (hasStock(id)) {
            throw new StoredItemsException(id);
        } else {
            repository.deleteById(id);
        }
    }

    /**
     * This method fetches the item by id from database and
     * returns true if there is an existing storageBin or drivingOrder with the itemNumber.
     *
     * @param id of the item
     * @return boolean
     */

    public boolean hasStock(String id) {
        Item item = repository.findById(id).orElseThrow(ItemNotFoundException::new);
        boolean isExistingInStorageBin = storageBinService.existsByItemNumber(item.itemNumber());
        boolean isExistingInDrivingOrders = drivingOrderRepo.existsByItemNumber(item.itemNumber());
        return isExistingInDrivingOrders || isExistingInStorageBin;
    }

    /**
     * This method throws an ItemAlreadyExistsException
     * when an item with the addItemDto ean or itemNumber is already existing.
     *
     * @param addItemDto fields ean and itemNumber
     */

    public void checkItemExisting(AddItemDto addItemDto) {
        boolean eanIsAlreadySaved = repository.existsByEan(addItemDto.ean());
        boolean itemNumberAlreadySaved = repository.existsByItemNumber(addItemDto.itemNumber());
        if (eanIsAlreadySaved || itemNumberAlreadySaved) {
            throw new ItemAlreadyExistsException(addItemDto.ean());
        }
    }

    /**
     * This method throws an ItemValidationException
     * when the addItemDto fields -> storable value and itemNumber are lower 0.
     *
     * @param addItemDto fields storableValue and itemNumber
     */
    public void validateAddItemDto(AddItemDto addItemDto) {
        boolean invalidCapacity = addItemDto.storableValue() < 0;
        boolean invalidItemNumber = addItemDto.itemNumber() < 0;
        if (invalidItemNumber || invalidCapacity) {
            throw new ItemValidationException(addItemDto.itemNumber(), addItemDto.storableValue());
        }
    }

    /**
     * This method returns true when addItemDto fields contains null , 0 or an empty String.
     *
     * @param addItemDto fields ean , itemNumber and storableValue
     * @return boolean
     */
    public boolean isNullOrEmpty(AddItemDto addItemDto) {
        if (addItemDto.ean() == null || addItemDto.itemNumber() == 0 || addItemDto.storableValue() == 0) {
            return true;
        }
        String emptyString = "";
        return emptyString.equals(addItemDto.ean());
    }
}
