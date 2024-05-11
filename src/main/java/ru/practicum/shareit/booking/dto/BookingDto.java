package ru.practicum.shareit.booking.dto;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private Long id;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private ItemDto item;
    private UserDto booker;
    private UserDto owner;
    private Long itemId;
    private Status status;

    public boolean validateRentDates() {
        return (this.start != null &&
                this.end != null &&
                this.start.isAfter(LocalDateTime.now()) &&
                this.end.isAfter(LocalDateTime.now()) &&
                this.start.isBefore(this.end));
    }
}
