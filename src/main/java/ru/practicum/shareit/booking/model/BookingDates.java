package ru.practicum.shareit.booking.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDates {
    private Long itemId;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
}
