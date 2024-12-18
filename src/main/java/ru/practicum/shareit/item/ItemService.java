package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, UpdateItemRequest itemRequest);

    ItemWithDateDto findItemById(Long itemId);

    Collection<ItemWithDateDto> findAllUserItems(Long userId);

    Collection<ItemDto> findByQueryText(String text);

    ItemDto delete(Long userId, Long itemId);

    CommentDto addComment(Long bookerId, Long itemId, CreateCommentDto createCommentDto);
}
