package ru.practicum.shareit.gateway.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.item.client.ItemClient;
import ru.practicum.shareit.gateway.item.dto.CommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
public class ItemController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final ItemClient client;

    public ItemController(ItemClient client) {
        this.client = client;
    }

    @PostMapping
    public Object addItem(@RequestBody @Valid ItemDto itemDto,
                          @RequestHeader(USER_HEADER) Long userId) {

        return client.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Object editItem(@PathVariable Long itemId,
                           @RequestBody ItemDto itemDto,
                           @RequestHeader(value = USER_HEADER) Long userId) {
        return client.updateItem(itemId, userId, itemDto);
    }


    @GetMapping("/{itemId}")
    public Object getItemById(@PathVariable Long itemId,
                              @RequestHeader(USER_HEADER) long userId) {
        return client.getItemById(userId, itemId);
    }


    @GetMapping()
    public Object getItemsByOwner(@RequestHeader(USER_HEADER) Long userId,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "20") @Positive Integer size) {
        return client.getAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public Object searchItems(@RequestParam String text,
                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                              @RequestParam(defaultValue = "20") @Positive Integer size) {
        return client.searchItemsByText(text, from, size);
    }


    @PostMapping("/{itemId}/comment")
    public Object addComment(@RequestHeader(USER_HEADER) Long userId,
                             @PathVariable Long itemId,
                             @RequestBody @Valid CommentDto requestDto) {
        return client.addComment(userId, itemId, requestDto);
    }
}
