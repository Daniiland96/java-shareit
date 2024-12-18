package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.StateRequest;

import java.util.List;

public interface BookingService {
    BookingDto create(Long bookerId, CreateBookingDto bookingDto);

    BookingDto updateStatus(Long userId, Long bookingId, Boolean approved);

    BookingDto findById(Long userId, Long bookingId);

    List<BookingDto> findAllBookingsOfBooker(Long bookerId, StateRequest state);

    List<BookingDto> findAllBookingsOfOwner(Long ownerId, StateRequest state);
}
