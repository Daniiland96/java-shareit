package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessRightsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService requestService;
    private final BookingService bookingService;

    private UserDto owner;
    private ItemDto item;
    private UpdateItemRequest updateItemRequest;

    @BeforeEach
    void setUp() {
        owner = new UserDto(null, "Owner", "owner@yandex.ru");
        item = new ItemDto(null, "Item", "Some item", "true", null, null);
        owner = userService.create(owner);
        item = itemService.create(owner.getId(), item);
    }

    @Test
    void create() {
        ItemDto result = itemService.create(owner.getId(), item);
        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(item.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getAvailable(), equalTo(item.getAvailable()));
        assertThat(result.getUserId(), equalTo(owner.getId()));
        assertThat(result.getRequestId(), nullValue());
    }

    @Test
    void createByRequest() {
        CreateItemRequestDto createItemRequestDto = new CreateItemRequestDto();
        createItemRequestDto.setDescription("ItemRequestDto");
        ItemRequestDto requestDto = requestService.create(owner.getId(), createItemRequestDto);
        item.setRequestId(requestDto.getId());
        ItemDto result = itemService.create(owner.getId(), item);

        assertThat(result, notNullValue());
        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(item.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getAvailable(), equalTo(item.getAvailable()));
        assertThat(result.getUserId(), equalTo(owner.getId()));
        assertThat(result.getRequestId(), notNullValue());
        assertThat(result.getRequestId(), equalTo(requestDto.getId()));
    }

    @Test
    void createByNoExistUser() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> itemService.create(999L, item));
        assertThat(exception.getMessage(), equalTo("user not found"));
    }

    @Test
    void createByNoExistRequest() {
        item.setRequestId(999L);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> itemService.create(owner.getId(), item));
        assertThat(exception.getMessage(), equalTo("request not found"));
    }

    @Test
    void update() {
        updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Update");
        updateItemRequest.setDescription("Update item");
        updateItemRequest.setAvailable("false");
        ItemDto result = itemService.update(item.getUserId(), item.getId(), updateItemRequest);

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(item.getId()));
        assertThat(result.getName(), equalTo(updateItemRequest.getName()));
        assertThat(result.getDescription(), equalTo(updateItemRequest.getDescription()));
        assertThat(result.getAvailable(), equalTo(updateItemRequest.getAvailable()));
        assertThat(result.getUserId(), equalTo(owner.getId()));
        assertThat(result.getRequestId(), nullValue());
    }

    @Test
    void updateByNoUser() {
        updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Update");
        updateItemRequest.setDescription("Update item");
        updateItemRequest.setAvailable("false");

        AccessRightsException exception = assertThrows(AccessRightsException.class, () -> itemService.update(999L, item.getId(), updateItemRequest));
        assertThat(exception.getMessage(), equalTo("no rights to update item"));
    }

    @Test
    void updateName() {
        updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Update");
        ItemDto result = itemService.update(item.getUserId(), item.getId(), updateItemRequest);

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(item.getId()));
        assertThat(result.getName(), equalTo(updateItemRequest.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getAvailable(), equalTo(item.getAvailable()));
        assertThat(result.getUserId(), equalTo(owner.getId()));
        assertThat(result.getRequestId(), nullValue());
    }

    @Test
    void findItemById() {
        ItemWithDateDto result = itemService.findItemById(item.getId());
        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(item.getId()));
        assertThat(result.getName(), equalTo(item.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getAvailable().toString(), equalTo(item.getAvailable()));
        assertThat(result.getUserId(), equalTo(owner.getId()));
        assertThat(result.getRequestId(), nullValue());
        assertThat(result.getComments(), empty());
    }

    @Test
    void findAllUserItems() {
        List<ItemWithDateDto> result = (List<ItemWithDateDto>) itemService.findAllUserItems(owner.getId());
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getName(), equalTo(item.getName()));
    }

    @Test
    void findByQueryTextName() {
        List<ItemDto> result = (List<ItemDto>) itemService.findByQueryText(item.getName());
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getName(), equalTo(item.getName()));
        assertThat(result.getFirst().getDescription(), equalTo(item.getDescription()));
    }

    @Test
    void findByQueryTextDescription() {
        List<ItemDto> result = (List<ItemDto>) itemService.findByQueryText(item.getDescription());
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.getFirst().getName(), equalTo(item.getName()));
        assertThat(result.getFirst().getDescription(), equalTo(item.getDescription()));
    }

    @Test
    void findByEmptyQueryText() {
        List<ItemDto> result = (List<ItemDto>) itemService.findByQueryText("");
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(0));
    }

    @Test
    void delete() {
        itemService.delete(owner.getId(), item.getId());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> itemService.findItemById(item.getId()));
        assertThat(exception.getMessage(), equalTo("item not found"));
    }

    @Test
    void deleteByNoUser() {
        AccessRightsException exception = assertThrows(AccessRightsException.class, () -> itemService.delete(999L, item.getId()));
        assertThat(exception.getMessage(), equalTo("no rights to delete item"));
    }

    @Test
    void addComment() throws InterruptedException {
        UserDto booker = new UserDto(null, "Booker", "booker@yandex.ru");
        booker = userService.create(booker);
        CreateBookingDto createBookingDto = new CreateBookingDto(null, LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2), item.getId(), BookingStatus.APPROVED);
        BookingDto bookingDto = bookingService.create(booker.getId(), createBookingDto);

        TimeUnit.SECONDS.sleep(3);

        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setText("Good item");
        CommentDto commentDto = itemService.addComment(booker.getId(), item.getId(), createCommentDto);

        assertThat(commentDto, notNullValue());
        assertThat(commentDto.getId(), notNullValue());
        assertThat(commentDto.getText(), equalTo(createCommentDto.getText()));
        assertThat(commentDto.getItemId(), equalTo(item.getId()));
        assertThat(commentDto.getAuthorName(), equalTo(booker.getName()));
        assertThat(commentDto.getCreated(), notNullValue());
    }

    @Test
    void addCommentByNotBooking() throws InterruptedException {
        UserDto booker = new UserDto(null, "Booker", "booker@yandex.ru");
        booker = userService.create(booker);
        CreateBookingDto createBookingDto = new CreateBookingDto(null, LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2), item.getId(), BookingStatus.WAITING);
        BookingDto bookingDto = bookingService.create(booker.getId(), createBookingDto);

        TimeUnit.SECONDS.sleep(3);

        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setText("Good item");

        UserDto finalBooker = booker;
        ValidationException exception = assertThrows(ValidationException.class, () -> itemService.addComment(finalBooker.getId(), item.getId(), createCommentDto));
        assertThat(exception.getMessage(), equalTo("user did not booking the item"));
    }
}