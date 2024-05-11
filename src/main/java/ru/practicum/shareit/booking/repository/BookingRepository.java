package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, int limit, int offset);

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId AND b.booking_start < :now AND b.booking_end > :now " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime now, int limit, int offset);

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId AND b.booking_end < :now " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime now, int limit, int offset);

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId AND b.booking_start > :now " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime now, int limit, int offset);


    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId AND b.status = :status " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, String status, int limit, int offset);


    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE i.owner = :ownerId " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId, int limit, int offset);

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE i.owner = :ownerId AND b.booking_start < :now AND b.booking_end > :now " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long ownerId, LocalDateTime now, int limit, int offset);

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE i.owner = :ownerId AND b.booking_end < :now " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime now, int limit, int offset);

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE i.owner = :ownerId AND b.booking_start > :now " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime now, int limit, int offset);

    @Query(nativeQuery = true,
            value = "SELECT * FROM booking AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN item i on i.id = b.item_id " +
                    "WHERE i.owner = :ownerId AND b.status = :status " +
                    "ORDER BY b.booking_start DESC LIMIT :limit OFFSET :offset")
    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, String status, int limit, int offset);


    List<Booking> findAllByItemIdAndStatusIsNot(Long itemId, Status status);

    boolean existsByItemIdAndBookerIdAndStatusAndEndIsBefore(Long itemId, Long bookerId, Status status, LocalDateTime now);

    List<Booking> findAllByItem_IdInAndStatusIsNot(List<Long> itemsIds, Status status);


}
