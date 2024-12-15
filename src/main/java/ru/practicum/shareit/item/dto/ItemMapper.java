package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.BookingDates;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable().toString(),
                item.getUser().getId()
        );
    }

    public static List<ItemDto> mapToItemDto(List<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(mapToItemDto(item));
        }
        return dtos;
    }

    public static Item mapToItem(ItemDto itemDto, User user) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                Boolean.valueOf(itemDto.getAvailable()),
                user
        );
    }

    public static Item updateUserFields(UpdateItemRequest itemRequest, Item item) {
        if (itemRequest.getName() != null) {
            item.setName(itemRequest.getName());
        }
        if (itemRequest.getDescription() != null) {
            item.setDescription(itemRequest.getDescription());
        }
        if (itemRequest.getAvailable() != null) {
            item.setAvailable(Boolean.valueOf(itemRequest.getAvailable()));
        }
        return item;
    }

    public static ItemWithDateDto mapToItemWithDateDto(Item item, BookingDates dates) {
        ItemWithDateDto dto = new ItemWithDateDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setUserId(item.getUser().getId());
        dto.setLastBooking(dates != null ? dates.getLastBooking() : null);
        dto.setNextBooking(dates != null ? dates.getNextBooking() : null);
        return dto;
    }

    public static List<ItemWithDateDto> mapToItemWithDateDto(List<Item> items, Map<Long, BookingDates> dates) {
        List<ItemWithDateDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(mapToItemWithDateDto(item, dates.get(item.getId())));
        }
        return result;
    }
}
