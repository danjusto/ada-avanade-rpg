package bootcamp.ada.avanade.rpg.dto.response;

import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;

public record BattleDTO(Long id, MonsterClass monster, Initiative initiative, CharacterResponseDTO character) {
}
