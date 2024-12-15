package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long bookerId, @Valid @RequestBody CreateBookingDto bookingDto) {
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
}
