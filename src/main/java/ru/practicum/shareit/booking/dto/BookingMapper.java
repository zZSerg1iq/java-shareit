package ru.practicum.shareit.booking.dto;


import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDto toDTO(Booking booking);

    Booking toEntity(BookingDto bookingDto);

    List<BookingDto> toDtoList(List<Booking> entities);
}
