package ru.practicum.shareit.server.request.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;

import java.util.List;

@Component
public interface RequestService {
    ItemRequestDto create(Long requesterId, ItemRequestDto requestDto);

    ItemRequestDto getByRequestId(Long userId, Long requestId);

    List<ItemRequestDto> getAllByRequesterId(Long requesterId);

    List<ItemRequestDto> getAll(Long userId, Integer from, Integer size);

}
