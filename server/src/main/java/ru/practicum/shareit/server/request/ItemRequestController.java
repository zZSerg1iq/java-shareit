package ru.practicum.shareit.server.request;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final RequestService requestService;

    public ItemRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(USER_HEADER) Long requesterId,
                                            @RequestBody ItemRequestDto requestDto) {
        return requestService.create(requesterId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByRequesterId(@RequestHeader(USER_HEADER) Long requesterId) {
        return requestService.getAllByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(USER_HEADER) Long userId,
                                       @RequestParam Integer from,
                                       @RequestParam Integer size) {

        return requestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getByRequestId(@RequestHeader(USER_HEADER) Long userId,
                                         @PathVariable Long requestId) {

        return requestService.getByRequestId(userId, requestId);
    }
}