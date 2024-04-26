package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime now, LocalDateTime timeNow);

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);


    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long ownerId, LocalDateTime now, LocalDateTime timeNow);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    List<Booking> findAllByItemIdAndStatusIsNot(Long itemId, Status status);


    boolean existsByItemIdAndBookerIdAndStatusAndEndIsBefore(Long itemId, Long bookerId, Status status, LocalDateTime now);

    List<Booking> findAllByItem_IdInAndStatusIsNot(List<Long> itemsIds, Status status);


}
