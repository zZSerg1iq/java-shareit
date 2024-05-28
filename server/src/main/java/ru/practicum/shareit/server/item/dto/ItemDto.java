package ru.practicum.shareit.server.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.user.dto.UserDto;


@Data
@Builder
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private UserDto owner;

    private UserDto renter;

    private ItemRequestDto itemRequest;

    private Long requestId;

}
