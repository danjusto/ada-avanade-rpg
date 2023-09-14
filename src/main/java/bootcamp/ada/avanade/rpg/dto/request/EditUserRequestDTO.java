package bootcamp.ada.avanade.rpg.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditUserRequestDTO(
        String name,
        @Email
        String email,
        @NotBlank
        @Size(min = 8, message = "The password must have at least 8 characters")
        String password) {
}
