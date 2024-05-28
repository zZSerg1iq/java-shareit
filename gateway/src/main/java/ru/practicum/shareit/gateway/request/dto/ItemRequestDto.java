package ru.practicum.shareit.gateway.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@Data
public class ItemRequestDto {

    private Long id;

    @NotBlank
    private String description;

    private LocalDateTime created;

    private Long requesterId;

    private List<ItemDto> items;
}
