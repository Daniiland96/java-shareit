package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.item.dal.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.mapToItem(itemDto);
        itemStorage.create(userId, item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, UpdateItemRequest itemRequest) {
        Item item = itemStorage.findItemById(itemId);
        if (item.getOwner() == null || !item.getOwner().getId().equals(userId)) {
            throw new AccessRightsException("no rights to update item");
        }
        ItemMapper.updateUserFields(itemRequest, item);
        itemStorage.update(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        Item item = itemStorage.findItemById(itemId);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Collection<ItemDto> findAllUserItems(Long userId) {
        return itemStorage.findAllUserItems(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> findByQueryText(String text) {
        if (text.isBlank()) {
            return Collections.EMPTY_LIST;
        }
        String[] strings = text.trim().split("[ ,.]");
        Set<String> stringsSet = Arrays.stream(strings)
                .filter(str -> !str.equals(" "))
                .collect(Collectors.toSet());
        List<Item> items = itemStorage.findByQueryText(stringsSet);
        return items.stream().map(ItemMapper::mapToItemDto).toList();
    }

    @Override
    public ItemDto delete(Long userId, Long itemId) {
        Item item = itemStorage.findItemById(itemId);
        if (!item.getOwner().getId().equals(userId)) {
            throw new AccessRightsException("no rights to delete item");
        }
        itemStorage.delete(itemId);
        return ItemMapper.mapToItemDto(item);
    }
}
