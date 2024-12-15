package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDates;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.status = 'APPROVED' " +
            "and b.item.id = ?1 " +
            "and(?2 <= b.end and ?3 >= b.start)")
    List<Booking> findBookingsWithOverlappingDateRange(Long itemId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start <= ?2 and b.end > ?2 order by b.start desc")
    List<Booking> findByBookerIdAndCurrentBookings(Long bookerId, LocalDateTime time);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime time);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime time);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findByItemUserIdOrderByStartDesc(Long ownerId);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.start <= ?2 and b.end > ?2 order by b.start desc")
    List<Booking> findByItemUserIdAndCurrentBookings(Long bookerId, LocalDateTime time);

    List<Booking> findByItemUserIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime time);

    List<Booking> findByItemUserIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime time);

    List<Booking> findByItemUserIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    @Query("select new ru.practicum.shareit.booking.model.BookingDates(b1.item.id, max(b1.end), min(b2.start)) from Booking b1 " +
            "inner join Booking b2 on b1.item.id = b2.item.id " +
            "where b1.item.id = ?1 and (b1.status = 'APPROVED' and b1.end <= ?2) and (b2.status = 'APPROVED' and b2.start >= ?2) " +
            "group by b1.item.id")
    BookingDates findBookingDates(Long itemId, LocalDateTime time);

    @Query("select new ru.practicum.shareit.booking.model.BookingDates(b1.item.id, max(b1.end), min(b2.start)) from Booking b1 " +
            "inner join Booking b2 on b1.item.id = b2.item.id " +
            "where b1.item.user.id = ?1 and (b1.status = 'APPROVED' and b1.end <= ?2) and (b2.status = 'APPROVED' and b2.start >= ?2) " +
            "group by b1.item.id")
    List<BookingDates> findBookingsDatesOfUser(Long userId, LocalDateTime time);
}
