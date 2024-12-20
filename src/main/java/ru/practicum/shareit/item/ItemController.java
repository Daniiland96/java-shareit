package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable(name = "itemId") Long itemId,
            @Valid @RequestBody UpdateItemRequest itemRequest
    ) {
        return itemService.update(userId, itemId, itemRequest);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@PathVariable(name = "itemId") Long itemId) {
        return itemService.findItemById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> findAllUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findByQueryText(@RequestParam(name = "text", required = false) String text) {
        return itemService.findByQueryText(text);
    }

    @DeleteMapping("/{itemId}")
    public ItemDto delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable(name = "itemId") Long itemId) {
        return itemService.delete(userId, itemId);
    }
}
