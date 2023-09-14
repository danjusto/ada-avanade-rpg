package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Orc implements CharacterClass {
    private final int healthPoints = 42;
    private final int strength = 7;
    private final int defense = 1;
    private final int agility = 2;
    private final int diceFaces = 4;
    private final int diceQty = 3;
}
