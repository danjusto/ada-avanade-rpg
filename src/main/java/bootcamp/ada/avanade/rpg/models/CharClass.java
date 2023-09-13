package bootcamp.ada.avanade.rpg.models;

import bootcamp.ada.avanade.rpg.models.characters.*;

public enum CharClass {
    WARRIOR(new Warrior()), BARBARIAN(new Barbarian()), KNIGHT(new Knight()), ORC(new Orc()), GIANT(new Giant()), WEREWOLF(new Werewolf());
    private CharacterClass charClass;
    CharClass(CharacterClass charClass) {
        this.charClass = charClass;
    }
    public CharacterClass getInstanceOfCharacterClass() {
        return charClass;
    }
}
