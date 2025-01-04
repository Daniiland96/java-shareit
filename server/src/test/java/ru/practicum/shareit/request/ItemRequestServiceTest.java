package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final ItemRequestService requestService;
    private final UserService userService;
    private final ItemService itemService;

    UserDto requestor;
    UserDto owner;
    CreateItemRequestDto createDto;
    ItemRequestDto requestDto;
    ItemDto item;

    @BeforeEach
    void setUp() {
        requestor = new UserDto(null, "Requestor", "requestor@yandex.ru");
        requestor = userService.create(requestor);
        owner = new UserDto(null, "Owner", "owner@yandex.ru");
        owner = userService.create(owner);

        createDto = new CreateItemRequestDto();
        createDto.setDescription("some itemRequest");
        requestDto = requestService.create(requestor.getId(), createDto);

        item = new ItemDto(null, "Item", "Some item", "true", null, requestDto.getId());
        item = itemService.create(owner.getId(), item);
    }

    @Test
    void create() {
        ItemRequestDto result = requestService.create(requestor.getId(), createDto);
        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getDescription(), equalTo(createDto.getDescription()));
        assertThat(result.getRequestorId(), equalTo(requestor.getId()));
        assertThat(result.getCreated(), notNullValue());
    }

    @Test
    void createByNotExistUser() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> requestService.create(999L, createDto));
        assertThat(exception.getMessage(), equalTo("user not found"));
    }

    @Test
    void findUserRequests() {
        List<ItemRequestWithItemsDto> result = (List<ItemRequestWithItemsDto>) requestService.findUserRequests(requestor.getId());
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getDescription(), equalTo(requestDto.getDescription()));
        assertThat(result.getFirst().getRequestorId(), equalTo(requestor.getId()));
    }

    @Test
    void findRequestsOfOtherUsers() {
        List<ItemRequestDto> result = (List<ItemRequestDto>) requestService.findRequestsOfOtherUsers(owner.getId());
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getDescription(), equalTo(requestDto.getDescription()));
        assertThat(result.getFirst().getRequestorId(), not(equalTo(owner.getId())));
    }

    @Test
    void findItemRequestById() {
        ItemRequestWithItemsDto result = requestService.findItemRequestById(requestDto.getId());
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(requestDto.getId()));
        assertThat(result.getDescription(), equalTo(requestDto.getDescription()));
        assertThat(result.getRequestorId(), equalTo(requestDto.getRequestorId()));
        assertThat(result.getCreated(), equalTo(requestDto.getCreated()));
        assertThat(result.getItems(), notNullValue());
        assertThat(result.getItems(), not(empty()));
    }

    @Test
    void findItemRequestByNotExistId() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> requestService.findItemRequestById(999L));
        assertThat(exception.getMessage(), equalTo("itemRequest not found"));
    }
}
