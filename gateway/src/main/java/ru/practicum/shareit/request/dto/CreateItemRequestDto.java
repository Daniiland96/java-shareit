package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateItemRequestDto {
    @NotBlank(message = "description cannot be null, empty or blank")
    @Size(max = 512, message = "description mustn't be over 512 characters")
    private String description;
}
