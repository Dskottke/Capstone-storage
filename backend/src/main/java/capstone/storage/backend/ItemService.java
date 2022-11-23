package capstone.storage.backend;

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

    public Item addItem(String eanToFind) {
        ItemResponse itemResponse = eanService.getItemResponse(eanToFind);
        Item itemToAdd = new Item(
                utils.generateUUID(),
                itemResponse.name(),
                itemResponse.categoryName(),
                itemResponse.issuingCountry(),
                itemResponse.ean(),
                STOREABLEVALUE_DEFAULT);
        return repository.save(itemToAdd);
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
}
