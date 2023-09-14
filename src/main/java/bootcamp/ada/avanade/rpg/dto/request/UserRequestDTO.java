package bootcamp.ada.avanade.rpg.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank
        String name,
        @NotBlank
        @Size(min = 6, message = "The username must have at least 6 characters")
        String username,
        @Email
        @NotNull
        String email,
        @NotBlank
        @Size(min = 8, message = "The password must have at least 8 characters")
        String password) {
}
