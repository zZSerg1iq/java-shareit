package ru.practicum.shareit.booking;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ExceptionMessages;
import ru.practicum.shareit.exceptions.ValidateException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
                                    @RequestBody @Valid BookingDto bookingDto) {
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
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "20") @Min(1) Integer size
    ) {
        return bookingService.getAllCustomerBookings(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsAtOwner(@RequestHeader(userIdHeaderName) Long ownerId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "20") @Min(1) Integer size) {

        return bookingService.getAllOwnerBookings(ownerId, state, from, size);
    }


}
