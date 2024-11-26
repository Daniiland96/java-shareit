package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User findUserById(Long id);

    List<User> findUsers();

    User create(User user);

    User update(User user);

    User delete(Long id);
}
