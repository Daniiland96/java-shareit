package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<UserDto> findUsers() {
        return UserMapper.mapToUserDto(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(Long id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found"));
        UserMapper.updateUserFields(userRequest, user);
        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
