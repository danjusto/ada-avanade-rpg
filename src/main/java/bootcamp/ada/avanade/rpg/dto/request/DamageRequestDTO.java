package bootcamp.ada.avanade.rpg.dto.request;

import jakarta.validation.constraints.NotNull;

public record DamageRequestDTO(@NotNull Long shiftId) {
}
