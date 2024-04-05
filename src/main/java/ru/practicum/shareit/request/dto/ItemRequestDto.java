package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */

@Builder
@Data
public class ItemRequestDto {
    private Long id;

    @NotBlank
    private String description;  // текст запроса, содержащий описание требуемой вещи;

    @NotEmpty
    private UserDto requestor;  // пользователь, создавший запрос;

    @NotEmpty
    private Date created;  // дата и время создания запроса.
}
