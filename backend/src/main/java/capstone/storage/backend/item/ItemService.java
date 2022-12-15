package capstone.storage.backend.item;

import capstone.storage.backend.drivingorders.DrivingOrderRepo;
import capstone.storage.backend.drivingorders.Type;
import capstone.storage.backend.drivingorders.models.DrivingOrder;
import capstone.storage.backend.exceptions.*;
import capstone.storage.backend.item.models.AddItemDto;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.item.models.Product;
import capstone.storage.backend.storagebin.StorageBinService;
import capstone.storage.backend.storagebin.models.StorageBin;
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


    public List<Item> findAll() {
        List<Item> allItems = repository.findAll();
        List<Item> listToReturn = new ArrayList<>();

        for (Item item : allItems) {
            int amount = storageBinService.getTotalAmountFromStorageBins(item);

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

    public Item updateItem(Item itemToUpdate) {
        if (isChangeable(itemToUpdate)) {
            throw new StorableValueUpdateException();
        } else {
            return repository.save(itemToUpdate);
        }
    }

    private boolean isChangeable(Item updateItem) {

        boolean storageIsValid = storedCheck(updateItem);
        boolean totalStockIsValid = totalStockCheck(updateItem);

        return storageIsValid && totalStockIsValid;
    }

    private boolean storedCheck(Item updateItem) {
        return storageBinService.findAllByItemNumber(updateItem.itemNumber())
                .stream()
                .anyMatch(storageBin -> storageBin.amount() > updateItem.storableValue());
    }

    private boolean totalStockCheck(Item updateItem) {
        return getTotalStock(updateItem)
                .stream()
                .anyMatch(storageBin -> storageBin.amount() > updateItem.storableValue());
    }

    private List<StorageBin> getTotalStock(Item itemToUpdate) {
        List<StorageBin> matchingStorageBins = storageBinService.findAllByItemNumber(itemToUpdate.itemNumber());
        return matchingStorageBins.stream()
                .map(storageBin -> locationMatches(storageBin, itemToUpdate))
                .toList();
    }

    private StorageBin locationMatches(StorageBin storageBin, Item itemToUpdate) {
        List<DrivingOrder> matchingOrders = drivingOrderRepo.findByTypeAndItemNumber(Type.INPUT, itemToUpdate.itemNumber());

        return matchingOrders.stream()
                .filter(drivingOrder -> drivingOrder.storageLocationId().equals(storageBin.locationId()))
                .map(drivingOrder -> calculateStock(storageBin, itemToUpdate, drivingOrder))
                .findFirst()
                .orElse(null);
    }

    private StorageBin calculateStock(StorageBin storageBin, Item itemToUpdate, DrivingOrder drivingOrder) {
        int totalStock = storageBin.amount() + drivingOrder.amount();
        return new StorageBin(storageBin.id(), storageBin.locationId(), itemToUpdate.itemNumber(), totalStock);
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

    public void deleteItemById(String id) {
        if (hasStock(id)) {
            throw new StoredItemsException(id);
        } else {
            repository.deleteById(id);
        }
    }

    public boolean hasStock(String id) {
        Item item = repository.findById(id).orElseThrow(ItemNotFoundException::new);
        boolean isExistingInStorageBin = storageBinService.existsByItemNumber(item.itemNumber());
        boolean isExistingInDrivingOrders = drivingOrderRepo.existsByItemNumber(item.itemNumber());
        return isExistingInDrivingOrders || isExistingInStorageBin;
    }


    public void checkItemExisting(AddItemDto addItemDto) {
        boolean eanIsAlreadySaved = repository.existsByEan(addItemDto.ean());
        boolean itemNumberAlreadySaved = repository.existsByItemNumber(addItemDto.itemNumber());
        if (eanIsAlreadySaved || itemNumberAlreadySaved) {
            throw new ItemAlreadyExistsException(addItemDto.ean());
        }
    }

    public void validateAddItemDto(AddItemDto addItemDto) {
        boolean invalidCapacity = addItemDto.storableValue() < 0;
        boolean invalidItemNumber = addItemDto.itemNumber() < 0;
        if (invalidItemNumber || invalidCapacity) {
            throw new ItemValidationException(addItemDto.itemNumber(), addItemDto.storableValue());
        }
    }

    public boolean isNullOrEmpty(AddItemDto addItemDto) {
        if (addItemDto.ean() == null || addItemDto.itemNumber() == 0 || addItemDto.storableValue() == 0) {
            return true;
        }
        String emptyString = "";
        return emptyString.equals(addItemDto.ean());
    }
}
