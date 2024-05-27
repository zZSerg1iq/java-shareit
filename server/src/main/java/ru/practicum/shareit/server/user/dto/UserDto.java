package ru.practicum.shareit.server.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long id;

    private String email;

    private String name;

}
