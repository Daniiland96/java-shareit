package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "email")
public class User {
    private Long id;
    @NotBlank(message = "email cannot be null, empty or blank")
    @Email(message = "email should be valid")
    private String email;
    @NotBlank(message = "name cannot be null, empty or blank")
    private String name;
}
