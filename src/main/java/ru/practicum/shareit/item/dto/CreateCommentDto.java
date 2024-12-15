package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCommentDto {
    @NotBlank(message = "text cannot be null, empty or blank")
    private String text;
}
