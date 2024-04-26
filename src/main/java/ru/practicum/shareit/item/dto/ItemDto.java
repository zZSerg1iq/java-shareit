package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
public class ItemDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private UserDto owner;

    private UserDto renter;

    private ItemRequestDto itemRequest;

}
