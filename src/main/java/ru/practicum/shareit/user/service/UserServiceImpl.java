package ru.practicum.shareit.user.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserDuplicateEmailException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Setter
    @Autowired
    private UserRepository repository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toDao(userDto);
        Optional<User> check = repository.findUserByEmail(userDto.getEmail());
        if (check.isPresent()) {
            throw new UserDuplicateEmailException();
        }

        user = repository.save(user);

        return UserMapper.toDto(user);
    }

    @Override
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

        return UserMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toDto(getById(userId));
    }

    @Override
    public List<UserDto> getAll() {
        return UserMapper.toDtoList(repository.findAll());
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
