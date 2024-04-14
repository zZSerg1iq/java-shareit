package ru.practicum.shareit.item.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Временное решение для этого 13 спринта, написано после написания JPA реализации
 * по тому что не проходили тесты в гите
 */
@Service
@Primary
public class ItemServiceInMemory implements ItemService {

    private final UserService userService;
    long id = 0L;
    private final Map<Long, Item> items;

    public ItemServiceInMemory(UserService userService) {
        this.userService = userService;
        items = new HashMap<>();
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = UserMapper.toDao(userService.getUserById(userId));
        Item item = ItemMapper.toDao(itemDto);

        if (user.getId().equals(userId)) {
            item.setOwner(user);
            item.setId(++id);
            items.put(item.getId(), item);
            user.getItemList().add(item);
            return ItemMapper.toDto(item);
        }

        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto editItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item item = ItemMapper.toDao(findItemById(itemId));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new UserNotFoundException();
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new ItemNotFoundException();
        }
        return ItemMapper.toDto(items.get(itemId));
    }

    @Override
    public List<ItemDto> findItemsByOwner(Long userId) {
        User user = UserMapper.toDao(userService.getUserById(userId));
        return ItemMapper.toListDto(user.getItemList());
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        final String name = text.toLowerCase();
        return ItemMapper.toListDto(items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(name) || item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList()));
    }
}
