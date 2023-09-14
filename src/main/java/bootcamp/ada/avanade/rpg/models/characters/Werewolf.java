package bootcamp.ada.avanade.rpg.models.characters;

import lombok.Getter;

@Getter
public class Werewolf implements CharacterClass {
    private final int healthPoints = 34;
    private final int strength = 7;
    private final int defense = 4;
    private final int agility = 7;
    private final int diceFaces = 4;
    private final int diceQty = 2;
}
