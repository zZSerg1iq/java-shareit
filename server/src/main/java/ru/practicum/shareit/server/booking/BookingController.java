package ru.practicum.shareit.server.booking;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.service.BookingService;
import ru.practicum.shareit.server.exceptions.ExceptionMessages;
import ru.practicum.shareit.server.exceptions.ValidateException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final String userIdHeaderName = "X-Sharer-User-Id";

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader(userIdHeaderName) Long renterId,
                                    @RequestBody @Validated BookingDto bookingDto) {
        if (!bookingDto.validateRentDates()) {
            throw new ValidateException(ExceptionMessages.VALIDATE_EXCEPTION.getMessage());
        }

        return bookingService.createBooking(bookingDto, renterId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader(userIdHeaderName) Long userId,
                                    @PathVariable final Long bookingId,
                                    @RequestParam boolean approved) {

        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(userIdHeaderName) Long userId,
                                     @PathVariable final Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsAtBooker(@RequestHeader(userIdHeaderName) Long bookerId,
                                                   @RequestParam String state,
                                                   @RequestParam Integer from,
                                                   @RequestParam Integer size
    ) {
        return bookingService.getAllCustomerBookings(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsAtOwner(@RequestHeader(userIdHeaderName) Long ownerId,
                                                  @RequestParam String state,
                                                  @RequestParam Integer from,
                                                  @RequestParam Integer size) {

        return bookingService.getAllOwnerBookings(ownerId, state, from, size);
    }


}
