package capstone.storage.backend.item;

import capstone.storage.backend.exceptions.IsNullOrEmptyException;
import capstone.storage.backend.exceptions.ItemISNotExistingException;
import capstone.storage.backend.exceptions.ItemToDeleteNotFoundException;
import capstone.storage.backend.item.models.AddItemDto;
import capstone.storage.backend.item.models.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items/")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping
    public List<Item> getAllItems() {
        return service.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Item saveItem(@RequestBody AddItemDto addItemDto) {

        if (service.isNullOrEmpty(addItemDto)) {
            throw new IsNullOrEmptyException();
        }
        return service.addItem(addItemDto);
    }

    @PutMapping()
    public ResponseEntity<Item> updateItem(@RequestBody Item itemToUpdate) {
        boolean itemExist = service.existById(itemToUpdate.id());
        if (itemExist) {
            Item updatedItem = service.updateItem(itemToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(updatedItem);
        } else {
            throw new ItemISNotExistingException();
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String id) {
        if (!service.existById(id)) {
            throw new ItemToDeleteNotFoundException(id);
        }
        service.deleteItemById(id);
    }


}
