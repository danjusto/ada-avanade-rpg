package bootcamp.ada.avanade.rpg.dto.response;

public record ShiftDTO(
        int round,
        int diceAtkCharacter,
        int diceDefMonster,
        int diceAtkMonster,
        int diceDefCharacter,
        Boolean characterHit,
        Boolean monsterHit,
        int diceDamageCharacter,
        int diceDamageMonster,
        int hpCharacter,
        int hpMonster
) {
}
