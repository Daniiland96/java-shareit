package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i join fetch i.user where i.id = ?1")
    Optional<Item> findByIdWithUser(Long itemId);

    @Query("select i from Item i join fetch i.user where i.user.id = ?1 and i.id = ?2")
    Optional<Item> findByIdAndUserIdWithUser(Long userId, Long itemId);

    @Query("select i from Item i join fetch i.user where i.user.id = ?1")
    List<Item> findAllWithUserByUserId(Long userId);

    @Query("select i from Item i join fetch i.user where (lower(i.name) like lower(?1) " +
            "or lower(i.description) like lower(?1)) and i.available = true")
    List<Item> findByQueryText(String text);
}
