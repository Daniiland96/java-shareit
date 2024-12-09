package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto findUserById(Long id);

    Collection<UserDto> findUsers();

    UserDto create(UserDto userDto);

    UserDto update(Long id, UpdateUserRequest userRequest);

    void delete(Long id);
}
