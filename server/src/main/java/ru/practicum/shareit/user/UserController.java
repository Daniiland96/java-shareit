package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable(name = "userId") Long userId) {
        return userService.findUserById(userId);
    }

    @GetMapping
    public Collection<UserDto> findUsers() {
        return userService.findUsers();
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable(name = "userId") Long userId, @RequestBody UpdateUserRequest userRequest) {
        return userService.update(userId, userRequest);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable(name = "userId") Long userId) {
        userService.delete(userId);
    }
}
