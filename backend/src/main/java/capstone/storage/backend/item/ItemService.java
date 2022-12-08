package capstone.storage.backend.item;

import capstone.storage.backend.drivingorders.DrivingOrderRepo;
import capstone.storage.backend.exceptions.ItemAlreadyExistException;
import capstone.storage.backend.exceptions.ItemISNotExistingException;
import capstone.storage.backend.exceptions.ItemValidationException;
import capstone.storage.backend.exceptions.StoredItemsException;
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
    private final ItemEanApiService eanService;
    private final StorageBinService storageBinService;
    private final DrivingOrderRepo drivingOrderRepo;
    private final ServiceUtils utils;


    public List<Item> findAll() {
        List<Item> allItems = repository.findAll();
        List<Item> listToReturn = new ArrayList<>();

        for (Item item : allItems) {
            String amount = storageBinService.getItemAmountFromStorageBinsByItemNumber(item);

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

    public Item addItem(AddItemDto addItemDto, String eanToFind) {

        validateAddItemDto(addItemDto);
        checkItemExisting(addItemDto, eanToFind);

        Product product = eanService.getItemResponseFromApi(eanToFind);


        String defaultStoringBinAmount = "0";
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

    public Item findItemByItemNumber(String itemNumber) {
        return repository.findItemByItemNumber(itemNumber).orElseThrow(ItemISNotExistingException::new);
    }

    public boolean existById(String id) {
        return repository.existsById(id);
    }

    public boolean existByItemNumber(String itemNumber) {
        return repository.existsByItemNumber(itemNumber);
    }

    public void deleteItemById(String id) {
        if (beforeDeleteControl(id)) {
            throw new StoredItemsException();
        } else {
            repository.deleteById(id);
        }
    }

    public boolean beforeDeleteControl(String id) {
        Item item = repository.findById(id).orElseThrow();
        boolean isExistingInStorageBin = storageBinService.existsByItemNumber(item.itemNumber());
        boolean isExistingInDrivingOrders = drivingOrderRepo.existsByItemNumber(item.itemNumber());
        return isExistingInDrivingOrders || isExistingInStorageBin;
    }

    public void checkItemExisting(AddItemDto addItemDto, String eanToFind) {
        boolean eanIsAlreadySaved = repository.existsByEan(eanToFind);
        boolean itemNumberAlreadySaved = repository.existsByItemNumber(addItemDto.itemNumber());
        if (eanIsAlreadySaved || itemNumberAlreadySaved) {
            throw new ItemAlreadyExistException();
        }
    }

    public void validateAddItemDto(AddItemDto addItemDto) {
        boolean invalidCapacity = Integer.parseInt(addItemDto.storableValue()) < 1;
        boolean invalidItemNumber = Integer.parseInt(addItemDto.itemNumber()) < 1;
        if (invalidItemNumber || invalidCapacity) {
            throw new ItemValidationException();
        }
    }

    public boolean isNullOrEmpty(AddItemDto addItemDto) {
        if (addItemDto.ean() == null || addItemDto.itemNumber() == null || addItemDto.storableValue() == null) {
            return true;
        }
        String emptyString = "";
        return emptyString.equals(addItemDto.itemNumber()) || emptyString.equals(addItemDto.storableValue()) || emptyString.equals(addItemDto.ean());
    }
}
