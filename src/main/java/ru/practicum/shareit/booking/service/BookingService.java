package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingDto requestDto, Long renterId);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto approveBooking(Long userId, Long bookingId, boolean approved);

    List<BookingDto> getAllCustomerBookings(Long bookerId, String state, int offset, int limit);

    List<BookingDto> getAllOwnerBookings(Long ownerId, String state, Integer offset, Integer limit);
}
