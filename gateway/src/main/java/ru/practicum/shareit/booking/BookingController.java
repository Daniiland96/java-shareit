package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @Valid @RequestBody CreateBookingDto bookingDto
    ) {
        return bookingClient.create(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable(name = "bookingId") Long bookingId,
            @RequestParam(name = "approved") Boolean approved
    ) {
        return bookingClient.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable(name = "bookingId") Long bookingId
    ) {
        return bookingClient.findById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingsOfBooker(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", required = false, defaultValue = "all") String stateParam
    ) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.findAllBookingsOfBooker(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingsOfOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", required = false, defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.findAllBookingsOfOwner(ownerId, state);
    }
}