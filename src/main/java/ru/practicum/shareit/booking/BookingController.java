package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ExceptionMessages;
import ru.practicum.shareit.exceptions.StatusException;
import ru.practicum.shareit.exceptions.ValidateException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final String userIdHeaderName = "X-Sharer-User-Id";

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

//    @ExceptionHandler
//    public ResponseEntity<Map<String, String>> handleValidateException(StatusException e) {
//        Map<String, String> map = new HashMap<>();
//        map.put("error", e.getMessage());
//        map.put("status", e.getMessage());
//        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

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
                                                   @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllCustomerBookings(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsAtOwner(@RequestHeader(userIdHeaderName) Long ownerId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllOwnerBookings(ownerId, state);
    }


}
