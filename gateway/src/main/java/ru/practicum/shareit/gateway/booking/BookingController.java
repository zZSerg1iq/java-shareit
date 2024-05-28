package ru.practicum.shareit.gateway.booking;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.booking.client.BookingClient;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;
import ru.practicum.shareit.gateway.exceptions.ExceptionMessages;
import ru.practicum.shareit.gateway.exceptions.ValidateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final String userIdHeaderName = "X-Sharer-User-Id";

    private final BookingClient client;

    public BookingController(BookingClient client) {
        this.client = client;
    }

    @PostMapping
    public Object createBooking(@RequestHeader(userIdHeaderName) Long renterId,
                                @RequestBody @Valid BookingDto bookingDto) {
        if (!bookingDto.validateRentDates()) {
            throw new ValidateException(ExceptionMessages.VALIDATE_EXCEPTION.getMessage());
        }
        return client.createBooking(renterId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Object updateBooking(@RequestHeader(userIdHeaderName) Long userId,
                                @PathVariable final Long bookingId,
                                @RequestParam boolean approved) {

        return client.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Object getBookingById(@RequestHeader(userIdHeaderName) Long userId,
                                 @PathVariable final Long bookingId) {
        return client.getBookingById(userId, bookingId);
    }

    @GetMapping
    public Object getAllBookingsAtBooker(@RequestHeader(userIdHeaderName) Long bookerId,
                                         @RequestParam(defaultValue = "ALL") String state,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "20") @Positive Integer size
    ) {
        return client.getAllBookingsAtBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public Object getAllBookingsAtOwner(@RequestHeader(userIdHeaderName) Long ownerId,
                                        @RequestParam(defaultValue = "ALL") String state,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "20") @Positive Integer size) {

        return client.getAllBookingsAtOwner(ownerId, state, from, size);
    }


}
