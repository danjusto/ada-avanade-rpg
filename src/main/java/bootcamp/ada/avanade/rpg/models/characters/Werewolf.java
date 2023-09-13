package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Werewolf implements CharacterClass {
    private int healthPoints = 34;
    private int strength = 7;
    private int defense = 4;
    private int agility = 7;
    private int diceFaces = 4;
    private int diceQty = 2;
}
