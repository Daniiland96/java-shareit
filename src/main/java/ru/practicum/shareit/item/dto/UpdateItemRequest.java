package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

@Data
@RequiredArgsConstructor
public class UpdateItemRequest {
    @Size(max = 200, message = "name mustn't be over 200 characters")
    private String name;
    @Size(max = 400, message = "description mustn't be over 400 characters")
    private String description;
    @Pattern(regexp = "^true?$|^false?$", message = "available allowed input true or false")
    private String available;
}
