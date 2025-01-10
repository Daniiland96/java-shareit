package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @Valid @RequestBody CreateItemRequestDto dto
    ) {
        return itemRequestClient.create(requestorId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> findUserRequests(
            @RequestHeader("X-Sharer-User-Id") Long requestorId
    ) {
        return itemRequestClient.findUserRequests(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findRequestsOfOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return itemRequestClient.findRequestsOfOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestById(@PathVariable(name = "requestId") Long requestId) {
        return itemRequestClient.findItemRequestById(requestId);
    }
}