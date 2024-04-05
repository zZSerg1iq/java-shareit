package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Setter
    @Autowired
    private UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        System.out.println(userDto);
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        System.out.println(userId);
        return userService.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        System.out.println(userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        System.out.println("get all");
        return userService.getAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        System.out.println(userId);
        userService.deleteUser(userId);
    }

}
