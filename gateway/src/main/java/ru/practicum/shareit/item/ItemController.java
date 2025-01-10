package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody ItemDto itemDto) {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable(name = "itemId") Long itemId,
            @Valid @RequestBody UpdateItemRequest itemRequest
    ) {
        return itemClient.update(userId, itemId, itemRequest);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@PathVariable(name = "itemId") Long itemId) {
        return itemClient.findItemById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.findAllUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByQueryText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "text", required = false) String text) {
        return itemClient.findByQueryText(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable(name = "itemId") Long itemId) {
        return itemClient.delete(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @PathVariable(name = "itemId") Long itemId,
            @Valid @RequestBody CreateCommentDto commentDto
    ) {
        return itemClient.addComment(bookerId, itemId, commentDto);
    }
}
