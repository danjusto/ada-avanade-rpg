package bootcamp.ada.avanade.rpg.models;

public enum MonsterClass {
    ORC(CharClass.ORC), GIANT(CharClass.GIANT), WEREWOLF(CharClass.WEREWOLF);
    private CharClass charClass;
    MonsterClass(CharClass charClass) {
        this.charClass = charClass;
    }
    public CharClass getEnumCharClass() {
        return charClass;
    }
}
