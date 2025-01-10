package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoTest {
    private final JacksonTester<ItemDto> json;

    @Test
    void itemDtoTest() throws Exception {
        ItemDto dto = new ItemDto(1L, "Test", "Some test", "false", 1L, 2L);

        JsonContent<ItemDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.userId").isEqualTo(dto.getUserId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(dto.getRequestId().intValue());
    }
}
