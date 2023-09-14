package bootcamp.ada.avanade.rpg.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordRequestDTO (
        @NotBlank
        String password,
        @NotBlank
        @Size(min = 8, message = "The password must have at least 8 characters")
        String newPassword) {
}
