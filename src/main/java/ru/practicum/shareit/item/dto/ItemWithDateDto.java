package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class ItemWithDateDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long userId;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
}
