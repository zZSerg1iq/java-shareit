package ru.practicum.shareit.server.booking.dto;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {

    private Long id;
    private LocalDateTime start;
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
