package ru.practicum.shareit.user.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class UserInMemory implements UserStorage {
    private Long id = 0L;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public User findUserById(Long id) {
        Optional<User> user = Optional.ofNullable(users.get(id));
        return user.orElseThrow(() -> new NotFoundException("user not found"));
    }

    @Override
    public List<User> findUsers() {
        return (List<User>) users.values();
    }

    @Override
    public User create(User user) {
        if (users.containsValue(user)) {
            throw new DuplicateDataException("email already exists");
        }
        user.setId(setUserId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        for (User existUser : users.values()) {
            if (existUser.equals(user) && !Objects.equals(existUser.getId(), user.getId())) {
                throw new DuplicateDataException("email already exists");
            }
        }
        return users.put(user.getId(), user);
    }

    @Override
    public User delete(Long id) {
        Optional<User> user = Optional.ofNullable(users.remove(id));
        return user.orElseThrow(() -> new NotFoundException("user not found"));
    }

    private Long setUserId() {
        return ++id;
    }
}
