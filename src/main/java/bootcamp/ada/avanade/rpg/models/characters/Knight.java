package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Knight implements CharacterClass {
    private int healthPoints = 26;
    private int strength = 6;
    private int defense = 8;
    private int agility = 3;
    private int diceFaces = 6;
    private int diceQty = 2;
}
