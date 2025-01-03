package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(
            @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @RequestBody CreateItemRequestDto dto
    ) {
        return itemRequestService.create(requestorId, dto);
    }

    @GetMapping
    public Collection<ItemRequestWithItemsDto> findUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long requestorId
    ) {
        return itemRequestService.findUserRequests(requestorId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findRequestsOfOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return itemRequestService.findRequestsOfOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto findItemRequestById(@PathVariable(name = "requestId") Long requestId) {
        return itemRequestService.findItemRequestById(requestId);
    }
}
