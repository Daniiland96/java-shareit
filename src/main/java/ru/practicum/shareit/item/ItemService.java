package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, UpdateItemRequest itemRequest);

    ItemDto findItemById(Long itemId);

    Collection<ItemDto> findAllUserItems(Long userId);

    Collection<ItemDto> findByQueryText(String text);

    ItemDto delete(Long userId, Long itemId);
}
