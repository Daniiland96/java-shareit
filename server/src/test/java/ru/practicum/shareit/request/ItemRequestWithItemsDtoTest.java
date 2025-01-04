package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.DataOfItem;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestWithItemsDtoTest {
    private final JacksonTester<ItemRequestWithItemsDto> json;

    @Test
    void itemRequestWithItemsDtoTest() throws Exception {
        DataOfItem dataOfItem = new DataOfItem();
        dataOfItem.setItemId(2L);
        dataOfItem.setName("Test");
        dataOfItem.setOwnerId(2L);

        ItemRequestWithItemsDto withItemsDto = new ItemRequestWithItemsDto();
        withItemsDto.setId(1L);
        withItemsDto.setDescription("ItemRequest controller testing");
        withItemsDto.setRequestorId(1L);
        withItemsDto.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        withItemsDto.setItems(List.of(dataOfItem));

        JsonContent<ItemRequestWithItemsDto> result = json.write(withItemsDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("ItemRequest controller testing");

        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(1);

        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(withItemsDto.getCreated().truncatedTo(ChronoUnit.SECONDS).toString());

        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Test");
    }
}
