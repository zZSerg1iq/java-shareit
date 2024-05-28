package ru.practicum.shareit.server.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.server.exceptions.UserNotFoundException;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.dto.UserMapper;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.toDao(userDto);
        user = repository.save(user);

        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = getById(userId);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            Optional<User> check = repository.findUserByEmail(userDto.getEmail());
            if (check.isPresent() && !check.get().equals(user)) {
                throw new UserDuplicateEmailException();
            }
            user.setEmail(userDto.getEmail());
        }
        repository.save(user);

        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.INSTANCE.toDto(getById(userId));
    }

    @Override
    public List<UserDto> getAll() {
        return UserMapper.INSTANCE.toDtoList(repository.findAll());
    }

    @Override
    public void deleteUser(Long userId) {
        repository.delete(getById(userId));
    }

    public User getById(Long userId) {
        Optional<User> temp = repository.findById(userId);

        if (temp.isEmpty()) {
            throw new UserNotFoundException();
        }
        return temp.get();
    }

}
