package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto findUserById(Long id);

    Collection<UserDto> findUsers();

    UserDto create(User user);

    UserDto update(Long id, UpdateUserRequest userRequest);

    UserDto delete(Long id);
}
