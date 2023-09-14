package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Giant implements CharacterClass {
    private final int healthPoints = 34;
    private final int strength = 10;
    private final int defense = 4;
    private final int agility = 4;
    private final int diceFaces = 6;
    private final int diceQty = 2;
}
