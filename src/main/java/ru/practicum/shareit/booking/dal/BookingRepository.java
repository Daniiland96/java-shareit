package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.status = 'APPROVED' " +
            "and b.item.id = ?1 " +
            "and(?2 <= b.end and ?3 >= b.start)")
    List<Booking> findBookingsWithOverlappingDateRange(Long itemId, LocalDateTime start, LocalDateTime end);
}
