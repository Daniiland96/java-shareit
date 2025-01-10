package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCommentDto {
    @NotBlank(message = "text cannot be null, empty or blank")
    private String text;
}
