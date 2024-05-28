package ru.practicum.shareit.gateway.user;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.user.client.UserClient;
import ru.practicum.shareit.gateway.user.dto.UserDto;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient client;

    public UserController(UserClient client) {
        this.client = client;
    }

    @PostMapping
    public Object addUser(@RequestBody @Validated UserDto userDto) {
        return client.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public Object updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        return client.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public Object getUserById(@PathVariable Long userId) {
        return client.getUserById(userId);
    }

    @GetMapping
    public Object getAllUsers() {
        return client.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        client.deleteUserById(userId);
    }

}
