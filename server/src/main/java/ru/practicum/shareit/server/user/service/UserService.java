package ru.practicum.shareit.server.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.user.dto.UserDto;

import java.util.List;

@Component
public interface UserService {


    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAll();

    void deleteUser(Long userId);
}
