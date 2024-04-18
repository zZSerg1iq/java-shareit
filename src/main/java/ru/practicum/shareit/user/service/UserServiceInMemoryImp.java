package ru.practicum.shareit.user.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

/**
 * Временное решение для этого 13 спринта, написано после написания JPA реализации
 * по тому что не проходили тесты в гите
 */
@Service
@Primary
public class UserServiceInMemoryImp implements UserService {

    private long userId = 0;
    private final Set<User> users;
    private final Map<Long, User> userMap;

    public UserServiceInMemoryImp() {
        this.users = new HashSet<>();
        this.userMap = new HashMap<>();
    }

    public User getUserDaoById(Long userId) {
        return userMap.get(userId);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toDao(userDto);

        if (!users.contains(user)) {
            user.setId(++userId);
            user.setItemList(new ArrayList<>());
            users.add(user);
            userMap.put(user.getId(), user);
            return UserMapper.toDto(user);
        }

        throw new UserDuplicateEmailException();
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = userMap.get(userId);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {

            for (User checkedUser : users) {
                if (checkedUser.getEmail().equals(userDto.getEmail()) &&
                        !checkedUser.equals(user)) {
                    throw new UserDuplicateEmailException();
                }
            }
            user.setEmail(userDto.getEmail());
        }


        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        return UserMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toDto(userMap.get(userId));
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userDtoList = new ArrayList<>(users);
        return UserMapper.toDtoList(userDtoList);
    }

    @Override
    public void deleteUser(Long userId) {
        if (userMap.containsKey(userId)) {
            var user = userMap.get(userId);
            users.remove(user);
            userMap.remove(userId);
        }
    }
}
