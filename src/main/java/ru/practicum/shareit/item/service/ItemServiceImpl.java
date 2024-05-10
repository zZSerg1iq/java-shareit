package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.REJECTED;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository repository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toDao(itemDto);

        User owner = findUserById(userId);
        item.setOwner(owner);
        item = repository.save(item);

        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto editItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item item = getItemById(itemId);

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new UserNotFoundException();
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        item = repository.save(item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        return ItemMapper.toDto(getItemById(itemId));
    }

    @Override
    public ItemDtoWithBooking findItemWithBookingById(Long itemId, Long ownerId) {

        ItemDtoWithBooking responseItem = ItemWithBookingsMapper.INSTANCE.toDto(repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вешь не найдена")));

        List<Comment> comments = commentRepository.findAllByItemIdOrderByCreatedDesc(itemId);
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(comment -> commentDtos.add(CommentMapper.INSTANCE.toDto(comment)));

        if (!repository.existsItemByIdAndOwner_Id(itemId, ownerId)) {
            responseItem.setComments(commentDtos);
            return responseItem;
        }

        List<BookingDto> bookings = BookingMapper.INSTANCE.toDtoList(bookingRepository.findAllByItemIdAndStatusIsNot(itemId, REJECTED));
        LocalDateTime now = LocalDateTime.now();

        return getItemWithBookingsAndCommentsDto(responseItem, commentDtos, bookings, now);
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        var a = repository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIs(text, text, true);
        return ItemMapper.toListDto(a);
    }

    private Item getItemById(Long itemId) {
        Optional<Item> optionalItem = repository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ItemNotFoundException();
        }
        return optionalItem.get();
    }

    private User findUserById(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    private ItemDtoWithBooking getItemWithBookingsAndCommentsDto(ItemDtoWithBooking item,
                                                                 List<CommentDto> comments,
                                                                 List<BookingDto> bookings,
                                                                 LocalDateTime now) {

        if (Objects.isNull(bookings)) {
            return item.toBuilder()
                    .lastBooking(null)
                    .nextBooking(null)
                    .comments(comments)
                    .build();
        }

        BookingDto first = bookings.stream()
                .filter(booking -> booking.getStart().isBefore(now))
                .max(Comparator.comparing(BookingDto::getStart))
                .orElse(null);

        BookingDto last = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(now))
                .min(Comparator.comparing(BookingDto::getStart))
                .orElse(null);

        BookingShortDto shortFirst = null;
        if (first != null) {
            shortFirst = new BookingShortDto();
            shortFirst.setId(first.getId() != null ? first.getId() : 0);
            shortFirst.setBookerId(first.getBooker().getId());
        }

        BookingShortDto shortLast = null;
        if (last != null) {
            shortLast = new BookingShortDto();
            shortLast.setId(last.getId());
            shortLast.setBookerId(last.getBooker().getId());
        }

        return item.toBuilder()
                .lastBooking(shortFirst)
                .nextBooking(shortLast)
                .comments(comments)
                .build();
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDTO) {
        User user = findUserById(userId);
        Item item = getItemById(itemId);
        LocalDateTime now = LocalDateTime.now();

        boolean isPresent = bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndIsBefore(itemId, userId, APPROVED, now);

        if (!isPresent) {
            throw new ValidateException("Booking bot found");
        }

        commentDTO.setCreated(now);
//        commentDTO.setAuthorId(userId);
//        commentDTO.setItemId(itemId);
//        ValidatorUtils.validate(commentDTO);

        Comment comment = CommentMapper.INSTANCE.toEntity(commentDTO);
        comment.setAuthor(user);
        comment.setItem(item);

        long id = commentRepository.save(comment).getId();

        commentDTO.setId(id);
        commentDTO.setAuthorName(user.getName());
        commentDTO.setAuthorId(user.getId());
        commentDTO.setItemId(itemId);
        return commentDTO;
    }


    @Override
    public List<ItemDtoWithBooking> findItemsByOwnerWithBookings(Long userId) {
        //repository.findItemsByOwnerId(userId);

        List<ItemDtoWithBooking> responseItems = ItemWithBookingsMapper.INSTANCE.toDtos(repository.findAllByOwnerIdOrderById(userId));

        List<Long> itemsIds = responseItems.stream()
                .map(ItemDtoWithBooking::getId)
                .collect(Collectors.toList());

        Map<Long, List<BookingDto>> bookings = bookingRepository.findAllByItem_IdInAndStatusIsNot(itemsIds, REJECTED).stream()
                .map(BookingMapper.INSTANCE::toDTO)
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        Map<Long, List<CommentDto>> comments = commentRepository.findAllByItemIdInOrderByCreatedDesc(itemsIds).stream()
                .map(CommentMapper.INSTANCE::toDto)
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        LocalDateTime now = LocalDateTime.now();

        return responseItems.stream().map(itemDto -> {
            Long itemId = itemDto.getId();
            return getItemWithBookingsAndCommentsDto(itemDto, comments.get(itemId), bookings.get(itemId), now);
        }).collect(Collectors.toList());
    }
}
