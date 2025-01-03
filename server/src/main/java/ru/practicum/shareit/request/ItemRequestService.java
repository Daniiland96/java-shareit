package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(Long requestorId, CreateItemRequestDto dto);

    List<ItemRequestWithItemsDto> findUserRequests(Long requestorId);

    List<ItemRequestDto> findRequestsOfOtherUsers(Long userId);

    ItemRequestWithItemsDto findItemRequestById(Long requestId);
}
