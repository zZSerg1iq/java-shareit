package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ItemWithBookingsMapper {

    ItemWithBookingsMapper INSTANCE = Mappers.getMapper(ItemWithBookingsMapper.class);

    ItemDtoWithBooking toDto(Item entity);

    List<ItemDtoWithBooking> toDtos(List<Item> entities);
}
