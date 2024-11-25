package ru.practicum.shareit.item.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemInMemory implements ItemStorage {
    private final UserStorage userStorage;

    private Long id = 0L;
    private Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Long userId, Item item) {
        User user = userStorage.findUserById(userId);
        item.setId(setItemId());
        item.setOwner(user);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public Item findItemById(Long itemId) {
        Optional<Item> item = Optional.ofNullable(items.get(itemId));
        return item.orElseThrow(() -> new NotFoundException("item not found"));
    }

    @Override
    public List<Item> findAllUserItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> findByQueryText(String text) {
        return null;
    }

    @Override
    public Item delete(Long itemId) {
        return items.remove(itemId);
    }

    private Long setItemId() {
        return ++id;
    }
}
