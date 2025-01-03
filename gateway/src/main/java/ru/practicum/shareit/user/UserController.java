package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUserById(@PathVariable(name = "userId") Long userId) {
        return userClient.findUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> findUsers() {
        return userClient.findUsers();
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable(name = "userId") Long userId,
                                         @Valid @RequestBody UpdateUserRequest userRequest) {
        return userClient.update(userId, userRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable(name = "userId") Long userId) {
        return userClient.delete(userId);
    }
}