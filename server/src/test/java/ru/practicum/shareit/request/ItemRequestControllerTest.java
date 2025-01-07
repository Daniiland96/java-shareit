package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.DataOfItem;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @MockBean
    private final ItemRequestService requestService;

    private final ObjectMapper mapper;
    private final MockMvc mvc;

    private ItemRequestDto dto;
    private ItemRequestWithItemsDto withItemsDto;
    private DataOfItem dataOfItem;

    @BeforeEach
    void setUp() {
        dto = new ItemRequestDto();
        dto.setId(1L);
        dto.setDescription("ItemRequest controller testing");
        dto.setRequestorId(1L);
        dto.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        dataOfItem = new DataOfItem();
        dataOfItem.setItemId(2L);
        dataOfItem.setName("Test");
        dataOfItem.setOwnerId(2L);

        withItemsDto = new ItemRequestWithItemsDto();
        withItemsDto.setId(1L);
        withItemsDto.setDescription("ItemRequest controller testing");
        withItemsDto.setRequestorId(1L);
        withItemsDto.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        withItemsDto.setItems(List.of(dataOfItem));
    }

    @Test
    void create() throws Exception {
        CreateItemRequestDto createDto = new CreateItemRequestDto();
        createDto.setDescription("ItemRequest controller testing");
        when(requestService.create(dto.getRequestorId(), createDto)).thenReturn(dto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(createDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", dto.getRequestorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.requestorId", is(dto.getRequestorId()), Long.class))
                .andExpect(jsonPath("$.created", is(dto.getCreated().truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    void findUserRequests() throws Exception {
        when(requestService.findUserRequests(withItemsDto.getRequestorId())).thenReturn(List.of(withItemsDto));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", withItemsDto.getRequestorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void findRequestsOfOtherUsers() throws Exception {
        when(requestService.findRequestsOfOtherUsers(dto.getRequestorId())).thenReturn(new ArrayList<>());

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", dto.getRequestorId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findItemRequestById() throws Exception {
        when(requestService.findItemRequestById(withItemsDto.getId())).thenReturn(withItemsDto);

        mvc.perform(get("/requests/" + withItemsDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(withItemsDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(withItemsDto.getDescription())))
                .andExpect(jsonPath("$.requestorId", is(withItemsDto.getRequestorId()), Long.class))
                .andExpect(jsonPath("$.created", is(withItemsDto.getCreated().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.items.length()").value(1));
    }
}
