package ru.practicum.shareit.server.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.server.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    public static ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequestDto toDto(ItemRequest entity);

    ItemRequest toEntity(ItemRequestDto dto);

    List<ItemRequestDto> toIDtoList(List<ItemRequest> enityList);

    List<ItemRequest> toEntityList(List<ItemRequestDto> dtoList);
}
