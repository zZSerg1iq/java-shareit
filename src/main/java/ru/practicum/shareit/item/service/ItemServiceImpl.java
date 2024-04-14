package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Класс для работы с товарами
 * пока не используется
 * Для следующего использования
 */
@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository repository;

    private UserRepository userRepository;

    public ItemServiceImpl(ItemRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toDao(itemDto);

        User owner = findUserById(userId);
        item.setOwner(owner);
        item = repository.save(item);

        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto editItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item item = getItem(itemId);

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

        item = repository.save(item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto findItemById(Long itemId) {
        return ItemMapper.toDto(getItem(itemId));
    }

    @Override
    public List<ItemDto> findItemsByOwner(Long userId) {
        return ItemMapper.toListDto(repository.findItemsByOwnerId(userId));
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        var a = repository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIs(text, text, true);
        System.out.println(a);
        return ItemMapper.toListDto(a);
    }

    private Item getItem(Long itemId) {
        Optional<Item> optionalItem = repository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ItemNotFoundException();
        }
        return optionalItem.get();
    }

    private User findUserById(Long userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }
}
