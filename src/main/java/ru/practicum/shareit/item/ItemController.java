package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto,
                           @RequestHeader(USER_HEADER) Long userId) {

        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@PathVariable Long itemId,
                            @RequestBody ItemDto itemDto,
                            @RequestHeader(value = USER_HEADER) Long userId) {
        return itemService.editItem(itemId, itemDto, userId);
    }


    @GetMapping("/{itemId}")
    public ItemDtoWithBooking getItemById(@PathVariable Long itemId,
                                          @RequestHeader(USER_HEADER) long userId) {
        return itemService.findItemWithBookingById(itemId, userId);
    }


    @GetMapping()
    public List<ItemDtoWithBooking> getItemsByOwner(@RequestHeader(USER_HEADER) Long userId) {
        return itemService.findItemsByOwnerWithBookings(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItemsByText(text);
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid CommentDto requestDto) {
        return itemService.addComment(userId, itemId, requestDto);
    }
}
