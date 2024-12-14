package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        Item item = ItemMapper.mapToItem(itemDto, user);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, UpdateItemRequest itemRequest) {
        Item item = itemRepository.findByIdWithUser(itemId)
                .orElseThrow(() -> new NotFoundException("item not found"));
        if (item.getUser() == null || !item.getUser().getId().equals(userId)) {
            throw new AccessRightsException("no rights to update item");
        }
        ItemMapper.updateUserFields(itemRequest, item);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        Item item = itemRepository.findByIdWithUser(itemId)
                .orElseThrow(() -> new NotFoundException("item not found"));
        User user = item.getUser();
        System.out.println(user);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemDto> findAllUserItems(Long userId) {
        return itemRepository.findAllWithUserByUserId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> findByQueryText(String text) {
        if (text.isBlank()) {
            return Collections.EMPTY_LIST;
        }
        List<Item> items = itemRepository.findByQueryText(text);
        return ItemMapper.mapToItemDto(items);
    }

    @Override
    @Transactional
    public ItemDto delete(Long userId, Long itemId) {
        Item item = itemRepository.findByIdAndUserIdWithUser(userId, itemId)
                .orElseThrow(() -> new AccessRightsException("no rights to delete item"));
        itemRepository.deleteById(itemId);
        return ItemMapper.mapToItemDto(item);
    }
}
