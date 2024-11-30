package ru.practicum.shareit.user;

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
    private String email;
    private String name;
}
