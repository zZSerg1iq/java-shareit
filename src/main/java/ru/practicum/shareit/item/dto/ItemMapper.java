package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static List<ItemDto> toListDto(List<Item> itemList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            itemDtoList.add(toDto(item));
        }

        return itemDtoList;
    }

    public static ItemDto toDto(Item item) {
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .available(item.isAvailable())
                .owner(UserMapper.toDto(item.getOwner()))
                .renter(item.getRenter() != null ? UserMapper.toDto(item.getRenter()) : null)
                .itemRequest(item.getRequest() != null ? RequestMapper.toDto(item.getRequest()) : null)
                .description(item.getDescription())
                .build();
    }

    public static Item toDao(ItemDto itemDto) {
        Item item = new Item();
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        item.setName(itemDto.getName());

        if (itemDto.getOwner() != null) {
            item.setOwner(UserMapper.toDao(itemDto.getOwner()));
        }

        if (itemDto.getRenter() != null) {
            item.setRenter(UserMapper.toDao(itemDto.getRenter()));
        }

        if (itemDto.getItemRequest() != null) {
            item.setRequest(RequestMapper.toDao(itemDto.getItemRequest()));
        }
        return item;
    }
}
