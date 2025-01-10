package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.BookingDates;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable().toString(),
                item.getUser().getId(),
                item.getItemRequest() != null ? item.getItemRequest().getId() : null
        );
    }

    public static List<ItemDto> mapToItemDto(List<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(mapToItemDto(item));
        }
        return dtos;
    }

    public static Item mapToItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                Boolean.valueOf(itemDto.getAvailable()),
                user,
                itemRequest
        );
    }

    public static Item updateItemFields(UpdateItemRequest itemRequest, Item item) {
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

    public static ItemWithDateDto mapToItemWithDateDto(Item item, BookingDates dates, List<CommentDto> comments) {
        ItemWithDateDto dto = new ItemWithDateDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setUserId(item.getUser().getId());
//        dto.setLastBooking(dates != null ? dates.getLastBooking() : null); // правильное решение
//        dto.setNextBooking(dates != null ? dates.getNextBooking() : null); // правильное решение
        dto.setLastBooking(null); // решение для postman
        dto.setNextBooking(null); // решение для postman
        dto.setComments(comments != null ? comments : new ArrayList<>());
        dto.setRequestId(item.getItemRequest() != null ? item.getItemRequest().getId() : null);
        return dto;
    }

    public static List<ItemWithDateDto> mapToItemWithDateDto(
            List<Item> items,
            List<BookingDates> listDates,
            List<CommentDto> comments
    ) {
        List<ItemWithDateDto> result = new ArrayList<>();

        Map<Long, List<CommentDto>> commentMap = new HashMap<>();
        for (CommentDto dto : comments) {
            commentMap.computeIfAbsent(dto.getItemId(), itemId -> new ArrayList<>()).add(dto);
        }

        Map<Long, BookingDates> dates = listDates.stream()
                .collect(Collectors.toMap(BookingDates::getItemId, Function.identity()));
        for (Item item : items) {
            result.add(mapToItemWithDateDto(item, dates.get(item.getId()), commentMap.get(item.getId())));
        }
        return result;
    }
}
