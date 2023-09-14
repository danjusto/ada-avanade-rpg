package bootcamp.ada.avanade.rpg.dto.request;

import bootcamp.ada.avanade.rpg.models.CharClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CharacterRequestDTO(
        @NotBlank
        String name,
        @NotNull
        CharClass characterClass) {
}
