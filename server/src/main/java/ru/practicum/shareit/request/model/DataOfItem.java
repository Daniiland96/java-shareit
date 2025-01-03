package ru.practicum.shareit.request.model;

import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class DataOfItem {
    private Long itemId;
    private String name;
    private Long ownerId;
}
