package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper
public interface ItemMapper {

    static ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    List<ItemDto> toListDto(List<Item> itemList);

    @Mapping(source = "request.id", target = "requestId")
    ItemDto toDto(Item item);

    Item toDao(ItemDto itemDto);
}
