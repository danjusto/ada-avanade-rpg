package bootcamp.ada.avanade.rpg.dto.request;

import bootcamp.ada.avanade.rpg.models.CharClass;

public record CharacterRequestDTO(String name, CharClass characterClass, Long userId) {
}
