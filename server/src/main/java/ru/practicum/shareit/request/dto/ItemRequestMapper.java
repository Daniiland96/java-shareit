package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.DataOfItem;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(CreateItemRequestDto dto, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestorId(itemRequest.getRequestor() != null ? itemRequest.getRequestor().getId() : null);
        dto.setCreated(itemRequest.getCreated());
        return dto;
    }

    public static List<ItemRequestDto> mapToItemRequestDto(List<ItemRequest> requests) {
        List<ItemRequestDto> dtos = new ArrayList<>();
        for (ItemRequest request : requests) {
            dtos.add(mapToItemRequestDto(request));
        }
        return dtos;
    }

    public static ItemRequestWithItemsDto mapToItemRequestDto(ItemRequest request, List<Item> items) {
        List<DataOfItem> dataOfItems = new ArrayList<>();
        for (Item item : items) {
            DataOfItem data = mapItemToDataOfItem(item);
            dataOfItems.add(data);
        }
        return mapRequestAndDataToDto(request, dataOfItems);
    }

    public static List<ItemRequestWithItemsDto> mapToItemRequestDto(List<ItemRequest> requests, List<Item> items) {
        Map<Long, List<DataOfItem>> itemsMap = new HashMap<>();
        for (Item item : items) {
            DataOfItem data = mapItemToDataOfItem(item);
            itemsMap.computeIfAbsent(item.getItemRequest().getId(), value -> new ArrayList<>()).add(data);
        }

        List<ItemRequestWithItemsDto> result = new ArrayList<>();
        for (ItemRequest request : requests) {
            ItemRequestWithItemsDto dto = mapRequestAndDataToDto(request, itemsMap.get(request.getId()));
            result.add(dto);
        }
        return result;
    }

    private static ItemRequestWithItemsDto mapRequestAndDataToDto(ItemRequest itemRequest, List<DataOfItem> items) {
        ItemRequestWithItemsDto dto = new ItemRequestWithItemsDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestorId(itemRequest.getRequestor() != null ? itemRequest.getRequestor().getId() : null);
        dto.setCreated(itemRequest.getCreated());
        dto.setItems(items);
        return dto;
    }

    private static DataOfItem mapItemToDataOfItem(Item item) {
        DataOfItem data = new DataOfItem();
        data.setItemId(item.getId());
        data.setName(item.getName());
        data.setOwnerId(item.getUser().getId());
        return data;
    }
}
