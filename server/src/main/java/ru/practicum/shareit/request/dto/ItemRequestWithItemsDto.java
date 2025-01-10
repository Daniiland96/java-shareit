package ru.practicum.shareit.request.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.model.DataOfItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class ItemRequestWithItemsDto {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private List<DataOfItem> items = new ArrayList<>();
}