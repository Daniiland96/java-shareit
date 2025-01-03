package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dal.ItemRequestRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto create(Long requestorId, CreateItemRequestDto dto) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(dto, requestor);
        return ItemRequestMapper.mapToItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestWithItemsDto> findUserRequests(Long requestorId) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId);
        List<Long> requestIds = requests.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRepository.findAllByItemRequestIdIn(requestIds);
        return ItemRequestMapper.mapToItemRequestDto(requests, items);
    }

    @Override
    public List<ItemRequestDto> findRequestsOfOtherUsers(Long userId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdNotOrderByCreatedDesc(userId);
        return ItemRequestMapper.mapToItemRequestDto(requests);
    }

    @Override
    public ItemRequestWithItemsDto findItemRequestById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("itemRequest not found"));
        List<Item> items = itemRepository.findAllByItemRequestId(requestId);
        return ItemRequestMapper.mapToItemRequestDto(itemRequest, items);
    }
}