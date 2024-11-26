package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemStorage {
    Item create(Long userId, Item item);

    Item update(Item item);

    Item findItemById(Long itemId);

    List<Item> findAllUserItems(Long userId);

    List<Item> findByQueryText(Set<String> strings);

    Item delete(Long itemId);
}
