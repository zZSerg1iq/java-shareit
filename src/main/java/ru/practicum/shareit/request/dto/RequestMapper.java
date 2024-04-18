package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static List<ItemRequestDto> toListDto(List<ItemRequest> requestList) {
        List<ItemRequestDto> itemDtoList = new ArrayList<>();
        for (ItemRequest item : requestList) {
            itemDtoList.add(toDto(item));
        }

        return itemDtoList;
    }

    public static ItemRequestDto toDto(ItemRequest request) {
        return ItemRequestDto
                .builder()
                .description(request.getDescription())
                .requestor(UserMapper.toDto(request.getRequestor()))
                .created(request.getCreated())
                .id(request.getId())
                .build();
    }

    public static ItemRequest toDao(ItemRequestDto requestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(requestDto.getCreated());
        itemRequest.setDescription(requestDto.getDescription());
        itemRequest.setRequestor(UserMapper.toDao(requestDto.getRequestor()));
        return itemRequest;
    }


}
