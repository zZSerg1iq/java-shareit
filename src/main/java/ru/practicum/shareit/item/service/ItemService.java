package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {


    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto editItem(Long itemId, ItemDto itemDto, Long ownerId);

    ItemDto findItemById(Long itemId);

    ItemDtoWithBooking findItemWithBookingById(Long itemId, Long ownerId);

    List<ItemDtoWithBooking> findItemsByOwnerWithBookings(Long userId, Integer from, Integer size);

    List<ItemDto> searchItemsByText(String text, Integer from, Integer size);

    CommentDto addComment(Long userId, Long itemId, CommentDto requestDto);
}
