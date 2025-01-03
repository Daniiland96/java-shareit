package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.StateRequest;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestBody CreateBookingDto bookingDto
    ) {
        return bookingService.create(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable(name = "bookingId") Long bookingId,
            @RequestParam(name = "approved") Boolean approved
    ) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable(name = "bookingId") Long bookingId
    ) {
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> findAllBookingsOfBooker(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") StateRequest state
    ) {
        return bookingService.findAllBookingsOfBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllBookingsOfOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") StateRequest state
    ) {
        return bookingService.findAllBookingsOfOwner(ownerId, state);
    }
}
