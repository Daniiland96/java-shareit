package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dal.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto create(Long bookerId, CreateBookingDto bookingDto) {
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("start date booking equal end date");
        }

        Item item = itemRepository.findByIdWithUser(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("item not found"));
        if (!item.getAvailable()) {
            throw new ValidationException("item not available");
        }

        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("booker not found"));

        List<Booking> overlappingBookings = bookingRepository
                .findBookingsWithOverlappingDateRange(item.getId(), bookingDto.getStart(), bookingDto.getEnd());
        if (!overlappingBookings.isEmpty()) {
            throw new DuplicateDataException("overlapping bookings");
        }

        Booking booking = BookingMapper.mapCreateDtoToBooking(bookingDto, item, booker);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking not found"));
        if (Objects.equals(booking.getItem().getUser().getId(), userId) && approved) {
            List<Booking> overlappingBookings = bookingRepository.findBookingsWithOverlappingDateRange(
                    booking.getItem().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
            if (!overlappingBookings.isEmpty()) {
                throw new DuplicateDataException("overlapping bookings");
            }
            booking.setStatus(BookingStatus.APPROVED);
            return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        }
        if (Objects.equals(booking.getItem().getUser().getId(), userId) && !approved) {
            booking.setStatus(BookingStatus.REJECTED);
            return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        }
        if (Objects.equals(booking.getBooker().getId(), userId) && !approved) {
            booking.setStatus(BookingStatus.CANCELED);
            return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
        }
        throw new AccessRightsException("no right to update booking");
    }
}
