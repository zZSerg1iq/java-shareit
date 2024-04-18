package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static List<UserDto> toDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();

        if (userList.size() > 0) {
            for (User u : userList) {
                userDtoList.add(toDto(u));
            }
        }
        return userDtoList;
    }

    public static UserDto toDto(User user) {
        return UserDto
                .builder()
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .build();
    }

    public static User toDao(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setId(userDto.getId());
        return user;
    }


}
