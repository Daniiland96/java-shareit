package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dal.UserStorage;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto findUserById(Long id) {
        User user = userStorage.findUserById(id);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<UserDto> findUsers() {
        return userStorage.findUsers().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto create(User user) {
        User newUser = userStorage.create(user);
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public UserDto update(Long id, UpdateUserRequest userRequest) {
        User user = userStorage.findUserById(id);
        User updateUser = new User(user.getId(), user.getEmail(), user.getName()); // Создаем новый объект, чтобы не изменить объект в хранилище
        // до проверки повторения email, с появление БД, эта строчка уйдет.
        UserMapper.updateUserFields(userRequest, updateUser);
        userStorage.update(updateUser);
        return UserMapper.mapToUserDto(updateUser);
    }

    @Override
    public UserDto delete(Long id){
        User user = userStorage.delete(id);
        return UserMapper.mapToUserDto(user);
    }
}
