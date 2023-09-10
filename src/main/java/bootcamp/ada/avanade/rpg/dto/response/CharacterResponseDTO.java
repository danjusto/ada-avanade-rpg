package bootcamp.ada.avanade.rpg.dto.response;

import bootcamp.ada.avanade.rpg.models.CharClass;

public record CharacterResponseDTO(Long id, String name, CharClass characterClass, int victories, int defeats, Long userId) {
}
