package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class BookingMapper {
    private static final String PATTERN = "yyyy.MM.dd hh:mm:ss";

    public static Booking mapToBooking(BookingDto dto, Item item, User booker) {
        return new Booking(
                dto.getId(),
                LocalDateTime.parse(dto.getStart()),
                LocalDateTime.parse(dto.getEnd()),
                item,
                booker,
                dto.getStatus() != null ? dto.getStatus() : null
        );
    }
}
