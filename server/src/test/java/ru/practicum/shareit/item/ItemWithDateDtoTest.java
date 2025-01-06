package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithDateDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemWithDateDtoTest {
    private final JacksonTester<ItemWithDateDto> json;

    @Test
    void itemWithDateDtoTest() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "Text", 1L, "Author Name",
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        ItemWithDateDto withDateDto = new ItemWithDateDto();
        withDateDto.setId(1L);
        withDateDto.setName("Test");
        withDateDto.setDescription("Some test");
        withDateDto.setAvailable(false);
        withDateDto.setUserId(1L);
        withDateDto.setLastBooking(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusDays(1));
        withDateDto.setNextBooking(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(1));
        withDateDto.setComments(List.of(commentDto));
        withDateDto.setRequestId(2L);

        JsonContent<ItemWithDateDto> result = json.write(withDateDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(withDateDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(withDateDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(withDateDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(withDateDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.userId").isEqualTo(withDateDto.getUserId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking")
                .isEqualTo(withDateDto.getLastBooking().truncatedTo(ChronoUnit.SECONDS).toString());
        assertThat(result).extractingJsonPathStringValue("$.nextBooking")
                .isEqualTo(withDateDto.getNextBooking().truncatedTo(ChronoUnit.SECONDS).toString());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(withDateDto.getRequestId().intValue());
    }
}
