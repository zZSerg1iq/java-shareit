package ru.practicum.shareit.server.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

@Mapper
public interface UserMapper {

    static UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<UserDto> toDtoList(List<User> userList);

    UserDto toDto(User user);

    User toDao(UserDto userDto);
}
