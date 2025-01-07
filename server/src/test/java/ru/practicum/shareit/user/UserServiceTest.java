package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(null, "Test", "test@yandex.ru");
        userDto = userService.create(userDto);
    }

    @Test
    void findUserById() {
        UserDto result = userService.findUserById(userDto.getId());
        assertThat(result.getId(), equalTo(userDto.getId()));
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void findUserByNoExistId() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findUserById(999L));
        assertThat("user not found", equalTo(exception.getMessage()));
    }

    @Test
    void findUsers() {
        Collection<UserDto> result = userService.findUsers();
        assertThat(result, notNullValue());
        assertThat(result, not(empty()));
    }

    @Test
    void create() {
        UserDto result = userService.create(userDto);
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void createWithDuplicateEmail() {
        userDto.setId(null);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class,
                () -> userService.create(userDto));
    }

    @Test
    void update() {
        UpdateUserRequest request = new UpdateUserRequest("Update", "update@yandex.ru");
        UserDto result = userService.update(userDto.getId(), request);
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(request.getName()));
        assertThat(result.getEmail(), equalTo(request.getEmail()));
    }

    @Test
    void updateName() {
        UpdateUserRequest request = new UpdateUserRequest("Update", null);
        UserDto result = userService.update(userDto.getId(), request);
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(request.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateEmail() {
        UpdateUserRequest request = new UpdateUserRequest(null, "update@yandex.ru");
        UserDto result = userService.update(userDto.getId(), request);
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(request.getEmail()));
    }

    @Test
    void delete() {
        userService.delete(userDto.getId());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findUserById(userDto.getId()));
        assertThat("user not found", equalTo(exception.getMessage()));
    }
}
