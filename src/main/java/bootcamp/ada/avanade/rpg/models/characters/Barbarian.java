package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Barbarian implements CharacterClass {
    private final int healthPoints = 21;
    private final int strength = 10;
    private final int defense = 2;
    private final int agility = 5;
    private final int diceFaces = 8;
    private final int diceQty = 2;
}
