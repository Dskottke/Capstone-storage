package capstone.storage.backend.item;

import capstone.storage.backend.exceptions.ExceptionMessage;
import capstone.storage.backend.exceptions.IsNullOrEmptyException;
import capstone.storage.backend.exceptions.ItemForbiddenRequestException;
import capstone.storage.backend.exceptions.ItemToDeleteNotFoundException;
import capstone.storage.backend.item.models.AddItemDto;
import capstone.storage.backend.item.models.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items/")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping
    public List<Item> getAllItems() {
        return service.findAll();
    }

    @PostMapping({"{eanToFind}", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public Item saveItem(@PathVariable Optional<String> eanToFind, @RequestBody AddItemDto addItemDto) {

        if (eanToFind.isEmpty() || service.isNullOrEmpty(addItemDto)) {
            throw new IsNullOrEmptyException(ExceptionMessage.IS_NULL_OR_EMPTY_EXCEPTION_MESSAGE.toString());
        }

        if (!addItemDto.ean().equals(eanToFind.get())) {
            throw new ItemForbiddenRequestException(ExceptionMessage.ITEM_FORBIDDEN_REQUEST_EXCEPTION_MESSAGE.toString());
        }
        return service.addItem(addItemDto, eanToFind.get());
    }

    @PutMapping("{ean}")
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody Item itemToUpdate) {
        if (itemToUpdate.id().equals(id)) {
            boolean itemExist = service.existById(id);
            Item updatedItem = service.updateItem(itemToUpdate);
            return itemExist ? ResponseEntity.status(HttpStatus.OK).body(updatedItem) : ResponseEntity.status(HttpStatus.CREATED).body(updatedItem);
        }
        throw new ItemForbiddenRequestException(ExceptionMessage.ITEM_FORBIDDEN_REQUEST_EXCEPTION_MESSAGE.toString());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String id) {
        if (!service.existById(id)) {
            throw new ItemToDeleteNotFoundException(ExceptionMessage.ITEM_TO_DELETE_NOT_FOUND_EXCEPTION_MESSAGE.toString());
        }
        service.deleteItemById(id);
    }

}
