package bootcamp.ada.avanade.rpg.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank
        String username,
        @NotBlank
        String password) {
}
