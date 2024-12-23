package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDates;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.CommentRepository;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

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
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found"));
        if (item.getUser() == null || !item.getUser().getId().equals(userId)) {
            throw new AccessRightsException("no rights to update item");
        }
        ItemMapper.updateItemFields(itemRequest, item);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemWithDateDto findItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found"));
        BookingDates bookingDates = bookingRepository.findBookingDates(itemId, LocalDateTime.now());
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return ItemMapper.mapToItemWithDateDto(item, bookingDates, CommentMapper.mapToCommentDto(comments));
    }

    @Override
    public Collection<ItemWithDateDto> findAllUserItems(Long userId) {
        List<Item> items = itemRepository.findAllByUserId(userId);
        List<BookingDates> listBookingDates = bookingRepository.findAllBookingsDatesOfUser(userId, LocalDateTime.now());
        List<Comment> comments = commentRepository.findAllByItemIdIn(items.stream().map(Item::getId).toList());
        List<CommentDto> commentDtos = CommentMapper.mapToCommentDto(comments);
        return ItemMapper.mapToItemWithDateDto(items, listBookingDates, commentDtos);
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
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found"));
        if (!Objects.equals(item.getUser().getId(), userId)) {
            throw new AccessRightsException("no rights to delete item");
        }
        itemRepository.deleteById(itemId);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public CommentDto addComment(Long bookerId, Long itemId, CreateCommentDto dto) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndItemIdAndStatusAndEndIsBefore(bookerId, itemId, BookingStatus.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("user did not booking the item");
        }
        Comment comment = CommentMapper.mapToComment(dto, booker, item);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }
}
