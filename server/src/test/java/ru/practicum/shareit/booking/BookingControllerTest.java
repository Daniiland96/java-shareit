package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {
    @MockBean
    private final BookingService bookingService;
    private final ObjectMapper mapper;
    private final MockMvc mvc;

    private CreateBookingDto createDto;
    private BookingDto dto;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        booker = new User(1L, "Booker", "booker@yandex.ru");
        item = new Item(1L, "Item", "Some item", true, null, null);

        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        dto = new BookingDto(1L, time, time.plusSeconds(1), item, booker, BookingStatus.APPROVED);
    }

    @Test
    void create() throws Exception {
        createDto = new CreateBookingDto(null, dto.getStart(), dto.getEnd(), item.getId(), dto.getStatus());
        when(bookingService.create(booker.getId(), createDto)).thenReturn(dto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(dto.getStart().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.end", is(dto.getEnd().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.booker.name", is(booker.getName())))
                .andExpect(jsonPath("$.status", is(dto.getStatus().toString())));
    }

    @Test
    void updateStatus () throws Exception {
        when(bookingService.updateStatus(booker.getId() + 1, dto.getId(), true)).thenReturn(dto);

        mvc.perform(patch("/bookings/" + dto.getId())
                        .param("approved", String.valueOf(true))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId() + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(dto.getStart().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.end", is(dto.getEnd().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.booker.name", is(booker.getName())))
                .andExpect(jsonPath("$.status", is(dto.getStatus().toString())));
    }
}
