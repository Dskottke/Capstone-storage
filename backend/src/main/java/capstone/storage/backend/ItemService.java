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
    private static final String STOREABLEVALUE_DEFAULT = "20";

    public List<Item> findAll() {
        return repository.findAll();
    }

    public Item addItem(AddItemDto addItemDto, String eanToFind) {
        ItemResponse itemResponse = eanService.getItemResponseFromApi(eanToFind);
        boolean itemExistingStatus = isItemExisting(addItemDto, eanToFind);
        if (itemExistingStatus) {
            throw new ItemAlreadyExistException("item is already saved");
        }
        Item itemToAdd = new Item(
                utils.generateUUID(),
                itemResponse.name(),
                itemResponse.categoryName(),
                itemResponse.issuingCountry(),
                itemResponse.ean(),
                STOREABLEVALUE_DEFAULT,
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

    public boolean isItemExisting(AddItemDto addItemDto, String eanToFind) {
        boolean eanIsAlreadySaved = repository.existsByEan(eanToFind);
        boolean itemNumberAlreadySaved = repository.existsByItemNumber(addItemDto.itemNumber());
        return (eanIsAlreadySaved || itemNumberAlreadySaved);
    }
}
