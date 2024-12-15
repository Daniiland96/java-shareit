package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.model.BookingDates;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithDateDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

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
        ItemMapper.updateUserFields(itemRequest, item);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemWithDateDto findItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found"));
        BookingDates bookingDates = bookingRepository.findBookingDates(itemId, LocalDateTime.now());
        return ItemMapper.mapToItemWithDateDto(item, bookingDates);
    }

    @Override
    public Collection<ItemWithDateDto> findAllUserItems(Long userId) {
        List<Item> items = itemRepository.findAllByUserId(userId);
        List<BookingDates> listDates = bookingRepository.findBookingsDatesOfUser(userId, LocalDateTime.now());
        Map<Long, BookingDates> dates = new HashMap<>();
        listDates.stream().peek(bookingDate -> dates.put(bookingDate.getItemId(), bookingDate));
        return ItemMapper.mapToItemWithDateDto(items, dates);
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
}
