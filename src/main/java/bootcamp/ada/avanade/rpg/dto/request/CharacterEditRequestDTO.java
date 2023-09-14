package bootcamp.ada.avanade.rpg.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CharacterEditRequestDTO(
        @NotBlank
        String name
) {
}
