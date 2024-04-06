package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto editItem(Long itemId, ItemDto itemDto, Long ownerId);

    ItemDto findItemById(Long itemId);

    List<ItemDto> findItemsByOwner(Long userId);

    List<ItemDto> searchItemsByText(String text);

}
