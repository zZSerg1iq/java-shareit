package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.AvailableException;
import ru.practicum.shareit.exceptions.ExceptionMessages;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.StatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.Status.REJECTED;
import static ru.practicum.shareit.booking.model.Status.WAITING;


public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto, Long renterId) {
        UserDto renter = userService.getUserById(renterId);
        ItemDto item = itemService.findItemById(bookingDto.getItemId());

        if (Objects.equals(renterId, item.getOwner().getId())) {
            throw new NotFoundException(" ");
        }
        if (!item.getAvailable()) {
            throw new AvailableException(ExceptionMessages.ITEM_NOT_AVAILABLE.getMessage());
        }

        bookingDto.setStatus(Status.WAITING);
        bookingDto.setBooker(renter);
        bookingDto.setItem(item);
        bookingDto.setOwner(item.getOwner());

        Booking booking = BookingMapper.INSTANCE.toEntity(bookingDto);

        long id = bookingRepository.save(booking).getId();
        bookingDto.setId(id);

        return bookingDto;
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Optional<Booking> bookingTemp = bookingRepository.findById(bookingId);
        if (bookingTemp.isEmpty()) {
            throw new NotFoundException(ExceptionMessages.BOOKING_NOT_FOUND.getMessage());
        }
        Booking booking = bookingTemp.get();
        if (booking.getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            return BookingMapper.INSTANCE.toDTO(booking);
        }
        throw new NotFoundException("Записей не найдено");
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        BookingDto bookingDto = getBookingById(userId, bookingId);

        if (bookingDto.getOwner().getId().equals(userId)) {
            if (bookingDto.getStatus() != Status.APPROVED) {
                bookingDto.setStatus(approved ? Status.APPROVED : Status.REJECTED);
                bookingRepository.save(BookingMapper.INSTANCE.toEntity(bookingDto));
                return bookingDto;
            }
            throw new AvailableException("Вешь недоступна");
        }
        throw new NotFoundException("Ничего не найдено");
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllCustomerBookings(Long bookerId, String state) {
        userService.getUserById(bookerId);

        switch (state) {
            case "ALL":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByBookerIdOrderByStartDesc(bookerId));
            case "CURRENT":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(bookerId, LocalDateTime.now(), LocalDateTime.now()));
            case "PAST":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByBookerIdAndEndIsBeforeOrderByStartDesc(bookerId, LocalDateTime.now()));
            case "FUTURE":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByBookerIdAndStartIsAfterOrderByStartDesc(bookerId, LocalDateTime.now()));
            case "WAITING":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(bookerId, WAITING));
            case "REJECTED":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(bookerId, REJECTED));
            default:
                throw new StatusException(String.format("Unknown state: %s", state));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllOwnerBookings(Long ownerId, String state) {
        userService.getUserById(ownerId);

        switch (state) {
            case "ALL":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByItemOwnerIdOrderByStartDesc(ownerId));
            case "CURRENT":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now()));
            case "PAST":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByItemOwnerIdAndEndIsBeforeOrderByStartDesc(ownerId, LocalDateTime.now()));
            case "FUTURE":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now()));
            case "WAITING":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, WAITING));
            case "REJECTED":
                return bookingEntityToDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, REJECTED));
            default:
                throw new StatusException(String.format("Unknown state: %s", state));
        }
    }

    private List<BookingDto> bookingEntityToDtoList(List<Booking> bookingDtoList) {
        return bookingDtoList
                .stream()
                .map(BookingMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }


}
