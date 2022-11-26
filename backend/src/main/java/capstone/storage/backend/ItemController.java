package capstone.storage.backend;

import capstone.storage.backend.exceptions.IsNullOrEmptyException;
import capstone.storage.backend.exceptions.ItemForbiddenRequestException;
import capstone.storage.backend.exceptions.ItemToDeleteNotFoundException;
import capstone.storage.backend.models.AddItemDto;
import capstone.storage.backend.models.Item;
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

    @PostMapping(value = {"{eanToFind}", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public Item saveItem(@PathVariable(required = false) String eanToFind, @RequestBody AddItemDto addItemDto) {

        if (eanToFind == null || service.isNullOrEmpty(addItemDto)) {
            throw new IsNullOrEmptyException("all input-fields must be filled");
        }
        if (!addItemDto.ean().equals(eanToFind)) {
            throw new ItemForbiddenRequestException("forbidden request");
        }
        return service.addItem(addItemDto, eanToFind);
    }
    @PutMapping("{id}")
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody Item itemToUpdate) {
        if (itemToUpdate.id().equals(id)) {
            boolean itemExist = service.existById(id);
            Item updatedItem = service.updateItem(itemToUpdate);
            return itemExist ? ResponseEntity.status(HttpStatus.OK).body(updatedItem) : new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new ItemForbiddenRequestException("forbidden request");
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String id) {
        if (!service.existById(id)) {
            throw new ItemToDeleteNotFoundException("item is already deleted");
        }
        service.deleteItemById(id);
    }

}
