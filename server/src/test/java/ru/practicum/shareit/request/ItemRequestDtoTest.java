package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void itemRequestDtoTest() throws Exception {

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(1L);
        dto.setDescription("ItemRequest controller testing");
        dto.setRequestorId(1L);
        dto.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        JsonContent<ItemRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("ItemRequest controller testing");

        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(1);

        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(dto.getCreated().truncatedTo(ChronoUnit.SECONDS).toString());
    }
}
