package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class UserDto {

    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    private List<ItemDto> itemList;

}
