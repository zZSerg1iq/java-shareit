package ru.practicum.shareit.gateway.request;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.request.client.RequestClient;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final RequestClient client;

    public ItemRequestController(RequestClient client) {
        this.client = client;
    }

    @PostMapping
    public Object createItemRequest(@RequestHeader(USER_HEADER) Long requesterId,
                                    @RequestBody @Validated ItemRequestDto requestDto) {
        return client.create(requesterId, requestDto);
    }

    @GetMapping
    public Object getAllByRequesterId(@RequestHeader(USER_HEADER) Long requesterId) {
        return client.getAllByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public Object getAll(@RequestHeader(USER_HEADER) Long userId,
                         @RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                         @RequestParam(defaultValue = "20") @Positive @Max(100) Integer size) {

        return client.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public Object getByRequestId(@RequestHeader(USER_HEADER) Long userId,
                                 @PathVariable Long requestId) {

        return client.getByRequestId(userId, requestId);
    }
}
