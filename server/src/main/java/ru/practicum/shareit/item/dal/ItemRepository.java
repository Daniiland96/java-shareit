package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserId(Long userId);

    @Query("select i from Item i where (lower(i.name) like lower(?1) " +
            "or lower(i.description) like lower(?1)) and i.available = true")
    List<Item> findByQueryText(String text);

//    @Query("select new ru.practicum.shareit.item.model.DataOfItem(i.id, i.name, i.user.id) " +
//            "from Item i where i.itemRequest.id = ?1")
//    List<DataOfItem> findShortDataOfItemsByItemRequestId(Long itemRequestId);

    List<Item> findAllByItemRequestIdIn(List<Long> requestIds);

    List<Item> findAllByItemRequestId(Long requestId);
}
