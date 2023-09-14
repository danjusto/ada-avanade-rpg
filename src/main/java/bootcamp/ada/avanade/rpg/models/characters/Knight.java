package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Knight implements CharacterClass {
    private final int healthPoints = 26;
    private final int strength = 6;
    private final int defense = 8;
    private final int agility = 3;
    private final int diceFaces = 6;
    private final int diceQty = 2;
}
