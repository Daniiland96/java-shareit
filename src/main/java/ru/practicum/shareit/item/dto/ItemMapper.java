package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable().toString(),
                item.getOwner()
        );
    }

    public static Item mapToItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                Boolean.valueOf(itemDto.getAvailable()),
                itemDto.getOwner()
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
}
