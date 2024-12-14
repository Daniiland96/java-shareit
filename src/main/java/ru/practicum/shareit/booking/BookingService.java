package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

public interface BookingService {
    BookingDto create(Long bookerId, CreateBookingDto bookingDto);
}
