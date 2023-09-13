package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Barbarian implements CharacterClass {
    private int healthPoints = 21;
    private int strength = 10;
    private int defense = 2;
    private int agility = 5;
    private int diceFaces = 8;
    private int diceQty = 2;
}
