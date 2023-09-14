package bootcamp.ada.avanade.rpg.dto.response;

import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;

import java.util.List;

public record BattleDetailsDTO(
        Long battleId,
        Long characterId,
        CharClass characterClass,
        String characterName,
        MonsterClass monsterClass,
        Initiative initiative,
        List<ShiftDTO> shifts
) {
}
