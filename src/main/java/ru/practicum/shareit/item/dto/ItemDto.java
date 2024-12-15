package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(message = "name cannot be null, empty or blank")
    @Size(max = 200, message = "name mustn't be over 200 characters")
    private String name;
    @NotBlank(message = "name cannot be null, empty or blank")
    @Size(max = 400, message = "description mustn't be over 400 characters")
    private String description;
    @NotBlank(message = "available cannot be null, empty or blank")
    @Pattern(regexp = "^true?$|^false?$", message = "available allowed input true or false")
    private String available;
    private Long userId;
}