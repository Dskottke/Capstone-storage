package capstone.storage.backend;

import capstone.storage.backend.exceptions.ItemAlreadyExistException;
import capstone.storage.backend.models.AddItemDto;
import capstone.storage.backend.models.Item;
import capstone.storage.backend.models.ItemResponse;
import capstone.storage.backend.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepo repository;
    private final EanApiService eanService;
    private final ServiceUtils utils;

    public List<Item> findAll() {
        return repository.findAll();
    }

    public Item addItem(AddItemDto addItemDto, String eanToFind) {

        validateAddItemDto(addItemDto);
        ItemResponse itemResponse = eanService.getItemResponseFromApi(eanToFind);
        isItemExisting(addItemDto, eanToFind);


        Item itemToAdd = new Item(
                utils.generateUUID(),
                itemResponse.name(),
                itemResponse.categoryName(),
                itemResponse.issuingCountry(),
                itemResponse.ean(),
                addItemDto.capacity(),
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

    public void isItemExisting(AddItemDto addItemDto, String eanToFind) {
        boolean eanIsAlreadySaved = repository.existsByEan(eanToFind);
        boolean itemNumberAlreadySaved = repository.existsByItemNumber(addItemDto.itemNumber());
        if (eanIsAlreadySaved || itemNumberAlreadySaved) {
            throw new ItemAlreadyExistException("item is already saved");
        }
    }

    public void validateAddItemDto(AddItemDto addItemDto) {
        boolean validCapacity = (Integer.parseInt(addItemDto.capacity()) < 1);
        boolean validItemNumber = (Integer.parseInt(addItemDto.itemNumber()) < 1);
        if (validItemNumber || validCapacity) {
            throw new IllegalArgumentException("capacity and the item-number must be higher than 0");
        }
    }
}
