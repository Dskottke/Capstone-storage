package capstone.storage.backend.item;

import capstone.storage.backend.exceptions.ItemAlreadyExistException;
import capstone.storage.backend.exceptions.ItemValidationException;
import capstone.storage.backend.item.models.AddItemDto;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.item.models.Product;
import capstone.storage.backend.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepo repository;
    private final ItemEanApiService eanService;
    private final ServiceUtils utils;

    public List<Item> findAll() {
        return repository.findAll();
    }

    public Item addItem(AddItemDto addItemDto, String eanToFind) {

        validateAddItemDto(addItemDto);
        checkItemExisting(addItemDto, eanToFind);

        Product product = eanService.getItemResponseFromApi(eanToFind);


        Item itemToAdd = new Item(
                utils.generateUUID(),
                product.name(),
                product.categoryName(),
                product.issuingCountry(),
                product.ean(),
                addItemDto.storableValue(),
                addItemDto.itemNumber());
        return repository.insert(itemToAdd);
    }

    public Item updateItem(Item articleRequest) {
        return repository.save(articleRequest);
    }

    public Item findItemByItemNumber(String itemNumber) {
        return repository.findItemByItemNumber(itemNumber);
    }

    public boolean existById(String id) {
        return repository.existsById(id);
    }

    public boolean existByItemNumber(String itemNumber) {
        return repository.existsByItemNumber(itemNumber);
    }

    public void deleteItemById(String id) {
        repository.deleteById(id);
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
