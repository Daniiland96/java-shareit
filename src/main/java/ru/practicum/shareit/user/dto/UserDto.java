package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "email cannot be null, empty or blank")
    @Email(message = "email should be valid")
    private String email;
    @NotBlank(message = "name cannot be null, empty or blank")
    private String name;
}
