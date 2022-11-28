package capstone.storage.backend.item;

import capstone.storage.backend.exceptions.ItemAlreadyExistException;
import capstone.storage.backend.exceptions.ItemValidationException;
import capstone.storage.backend.item.models.AddItemDto;
import capstone.storage.backend.item.models.Item;
import capstone.storage.backend.item.models.ItemResponse;
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

        ItemResponse itemResponse = eanService.getItemResponseFromApi(eanToFind);


        Item itemToAdd = new Item(
                utils.generateUUID(),
                itemResponse.name(),
                itemResponse.categoryName(),
                itemResponse.issuingCountry(),
                itemResponse.ean(),
                addItemDto.storableValue(),
                addItemDto.itemNumber());
        return repository.insert(itemToAdd);
    }

    public Item updateItem(Item articleRequest) {
        return repository.save(articleRequest);
    }

    public boolean existById(String id) {
        return repository.existsById(id);
    }

    public void deleteItemById(String id) {
        repository.deleteById(id);
    }

    public void checkItemExisting(AddItemDto addItemDto, String eanToFind) {
        boolean eanIsAlreadySaved = repository.existsByEan(eanToFind);
        boolean itemNumberAlreadySaved = repository.existsByItemNumber(addItemDto.itemNumber());
        if (eanIsAlreadySaved || itemNumberAlreadySaved) {
            throw new ItemAlreadyExistException("item is already existing");
        }
    }

    public void validateAddItemDto(AddItemDto addItemDto) {
        boolean validCapacity = (Integer.parseInt(addItemDto.storableValue()) < 1);
        boolean validItemNumber = (Integer.parseInt(addItemDto.itemNumber()) < 1);
        if (validItemNumber || validCapacity) {
            throw new ItemValidationException("capacity and the item-number must be greater than 0");
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
