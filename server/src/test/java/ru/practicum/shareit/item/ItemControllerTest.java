package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    private final ItemService itemService;

    private final ObjectMapper mapper;
    private final MockMvc mvc;

    private ItemDto dto;
    private ItemWithDateDto withDateDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        commentDto = new CommentDto(1L, "Text", 1L, "Author Name",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        dto = new ItemDto(1L, "Test", "Some test", "true", 1L, 11L);

        withDateDto = new ItemWithDateDto();
        withDateDto.setId(dto.getId());
        withDateDto.setName(dto.getName());
        withDateDto.setDescription(dto.getDescription());
        withDateDto.setAvailable(Boolean.valueOf(dto.getAvailable()));
        withDateDto.setUserId(dto.getUserId());
        withDateDto.setLastBooking(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusDays(1));
        withDateDto.setNextBooking(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(1));
        withDateDto.setComments(List.of(commentDto));
        withDateDto.setRequestId(dto.getRequestId());
    }

    @Test
    void create() throws Exception {

        when(itemService.create(dto.getUserId(), dto)).thenReturn(dto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", dto.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.available", is(dto.getAvailable())))
                .andExpect(jsonPath("$.userId", is(dto.getUserId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(dto.getRequestId()), Long.class));
    }

    @Test
    void update() throws Exception {
        UpdateItemRequest updateRequest = new UpdateItemRequest();
        when(itemService.update(dto.getUserId(), dto.getId(), updateRequest)).thenReturn(dto);

        mvc.perform(patch("/items/" + dto.getId())
                        .content(mapper.writeValueAsString(updateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", dto.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.available", is(dto.getAvailable())))
                .andExpect(jsonPath("$.userId", is(dto.getUserId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(dto.getRequestId()), Long.class));
    }

    @Test
    void findItemById() throws Exception {

        when(itemService.findItemById(withDateDto.getId())).thenReturn(withDateDto);

        mvc.perform(get("/items/" + withDateDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(withDateDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(withDateDto.getName())))
                .andExpect(jsonPath("$.description", is(withDateDto.getDescription())))
                .andExpect(jsonPath("$.available", is(withDateDto.getAvailable())))
                .andExpect(jsonPath("$.userId", is(withDateDto.getUserId()), Long.class))
                .andExpect(jsonPath("$.lastBooking",
                        is(withDateDto.getLastBooking().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.nextBooking",
                        is(withDateDto.getNextBooking().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.comments.length()").value(1))
                .andExpect(jsonPath("$.comments[0].text", is(commentDto.getText())))
                .andExpect(jsonPath("$.requestId", is(withDateDto.getRequestId()), Long.class));
    }

    @Test
    void findAllUserItems() throws Exception {

        when(itemService.findAllUserItems(withDateDto.getUserId())).thenReturn(List.of(withDateDto));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", withDateDto.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name", is(withDateDto.getName())));
    }

    @Test
    void findByQueryText() throws Exception {

        when(itemService.findByQueryText(dto.getDescription())).thenReturn(List.of(dto));

        mvc.perform(get("/items/search")
                        .param("text", dto.getDescription())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", withDateDto.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name", is(dto.getName())));
    }

    @Test
    void deleteItem() throws Exception {

        when(itemService.delete(dto.getUserId(), dto.getId())).thenReturn(dto);

        mvc.perform(delete("/items/" + dto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", dto.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.available", is(dto.getAvailable())))
                .andExpect(jsonPath("$.userId", is(dto.getUserId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(dto.getRequestId()), Long.class));
    }

    @Test
    void addComment() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        when(itemService.addComment(dto.getUserId() + 1, dto.getId(), createCommentDto)).thenReturn(commentDto);

        mvc.perform(post("/items/" + dto.getId() + "/comment")
                        .content(mapper.writeValueAsString(createCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", dto.getUserId() + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.itemId", is(commentDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().truncatedTo(ChronoUnit.SECONDS).toString())));
    }
}
