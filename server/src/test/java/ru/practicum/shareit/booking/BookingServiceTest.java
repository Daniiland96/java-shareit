package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateRequest;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    private UserDto owner;
    private UserDto booker;
    private ItemDto item;
    private BookingDto booking;
    private CreateBookingDto createBookingDto;

    @BeforeEach
    void setUp() {
        owner = new UserDto(null, "Owner", "owner@yandex.ru");
        booker = new UserDto(null, "Booker", "booker@yandex.ru");
        item = new ItemDto(null, "Item", "Some item", "true", null, null);
        owner = userService.create(owner);
        booker = userService.create(booker);
        item = itemService.create(owner.getId(), item);

        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        createBookingDto = new CreateBookingDto(null, time.plusDays(1), time.plusDays(2), item.getId(), null);
        booking = bookingService.create(booker.getId(), createBookingDto);
    }

    @Test
    void create() {
        assertThat(booking, notNullValue());
        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStart(), equalTo(createBookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(createBookingDto.getEnd()));
        assertThat(booking.getItem().getName(), equalTo(item.getName()));
        assertThat(booking.getBooker().getName(), equalTo(booker.getName()));
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void createBookingWithEqualStartAndEnd() {
        createBookingDto.setEnd(createBookingDto.getStart());
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.create(booker.getId(), createBookingDto));
        assertThat(exception.getMessage(), equalTo("start date booking equal end date"));
    }

    @Test
    void createBookingWithNotAvailableItem() {
        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setAvailable("false");
        itemService.update(owner.getId(), item.getId(), updateItemRequest);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.create(booker.getId(), createBookingDto));
        assertThat(exception.getMessage(), equalTo("item not available"));
    }

    @Test
    void createBookingWithOverlappingDate() {
        bookingService.updateStatus(owner.getId(), booking.getId(), true);
        DuplicateDataException exception = assertThrows(DuplicateDataException.class,
                () -> bookingService.create(booker.getId(), createBookingDto));
        assertThat(exception.getMessage(), equalTo("overlapping bookings"));
    }

    @Test
    void updateStatusToApproved() {
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
        booking = bookingService.updateStatus(owner.getId(), booking.getId(), true);
        assertThat(booking.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void updateStatusToRejected() {
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
        booking = bookingService.updateStatus(owner.getId(), booking.getId(), false);
        assertThat(booking.getStatus(), equalTo(BookingStatus.REJECTED));
    }

    @Test
    void updateStatusToCanceled() {
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
        booking = bookingService.updateStatus(booker.getId(), booking.getId(), false);
        assertThat(booking.getStatus(), equalTo(BookingStatus.CANCELED));
    }

    @Test
    void updateStatusWithOverlappingBooking() {
        createBookingDto.setStatus(BookingStatus.APPROVED);
        bookingService.create(booker.getId(), createBookingDto);
        DuplicateDataException exception = assertThrows(DuplicateDataException.class,
                () -> bookingService.updateStatus(owner.getId(), booking.getId(), true));
        assertThat(exception.getMessage(), equalTo("overlapping bookings"));
    }

    @Test
    void updateStatusByNotExistUser() {
        AccessRightsException exception = assertThrows(AccessRightsException.class,
                () -> bookingService.updateStatus(999L, booking.getId(), true));
        assertThat(exception.getMessage(), equalTo("no right to update booking"));
    }

    @Test
    void findById() {
        BookingDto result = bookingService.findById(owner.getId(), booking.getId());
        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getStart(), equalTo(createBookingDto.getStart()));
        assertThat(result.getEnd(), equalTo(createBookingDto.getEnd()));
        assertThat(result.getItem().getName(), equalTo(item.getName()));
        assertThat(result.getBooker().getName(), equalTo(booker.getName()));
        assertThat(result.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void findByIdNotExistBooking() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.findById(booker.getId(), 999L));
        assertThat(exception.getMessage(), equalTo("booking not found"));
    }

    @Test
    void findByIdNotUserBooking() {
        AccessRightsException exception = assertThrows(AccessRightsException.class,
                () -> bookingService.findById(999L, booking.getId()));
        assertThat(exception.getMessage(), equalTo("no right to find this booking"));
    }

    @Test
    void findAllBookingsOfBookerWithStateAll() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfBooker(booker.getId(), StateRequest.ALL);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getId(), equalTo(booking.getId()));
    }

    @Test
    void findAllBookingsOfBookerWithStateCurrent() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfBooker(booker.getId(), StateRequest.CURRENT);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    void findAllBookingsOfBookerWithStatePast() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfBooker(booker.getId(), StateRequest.PAST);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    void findAllBookingsOfBookerWithStateFuture() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfBooker(booker.getId(), StateRequest.FUTURE);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getId(), equalTo(booking.getId()));
    }

    @Test
    void findAllBookingsOfBookerWithStateWaiting() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfBooker(booker.getId(), StateRequest.WAITING);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getId(), equalTo(booking.getId()));
    }

    @Test
    void findAllBookingsOfBookerWithStateRejected() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfBooker(booker.getId(), StateRequest.REJECTED);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    void findAllBookingsOfOwnerWithStateAll() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfOwner(owner.getId(), StateRequest.ALL);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getId(), equalTo(booking.getId()));
    }

    @Test
    void findAllBookingsOfOwnerWithStateCurrent() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfOwner(owner.getId(), StateRequest.CURRENT);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    void findAllBookingsOfOwnerWithStatePast() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfOwner(owner.getId(), StateRequest.PAST);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(0));
    }

    @Test
    void findAllBookingsOfOwnerWithStateFuture() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfOwner(owner.getId(), StateRequest.FUTURE);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getId(), equalTo(booking.getId()));
    }

    @Test
    void findAllBookingsOfOwnerWithStateWaiting() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfOwner(owner.getId(), StateRequest.WAITING);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.getFirst().getId(), equalTo(booking.getId()));
    }

    @Test
    void findAllBookingsOfOwnerWithStateRejected() {
        List<BookingDto> bookings = bookingService.findAllBookingsOfOwner(owner.getId(), StateRequest.REJECTED);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(0));
    }
}
