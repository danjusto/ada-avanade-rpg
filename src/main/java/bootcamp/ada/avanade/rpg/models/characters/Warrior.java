package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Warrior implements CharacterClass {
    private final int healthPoints = 20;
    private final int strength = 7;
    private final int defense = 5;
    private final int agility = 6;
    private final int diceFaces = 12;
    private final int diceQty = 1;
}
