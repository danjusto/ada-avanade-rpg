package bootcamp.ada.avanade.rpg.models.damage;

import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;

import java.util.Objects;
import java.util.Random;

public abstract class Damage {
    protected static Random random;
    protected abstract void confirmHit(Shift shift);
    protected abstract void checkDuplicateDamage(Shift shift);
    protected void endBattle(Battle battle, Shift shift, Boolean victorie) {
        battle.endBattle();
        battle.getCharacter().updateVictoriesAndDefeats(victorie);
        shift.setActive(false);
    }
    protected int rollCustomDice(int faces, int numberDices) {
        int result = 0;
        int min = 1;
        for (int i = 0; i < numberDices; i++) {
            var shot = getRandom().nextInt(faces) + min;
            result += shot;
        }
        return result;
    }
    private static Random getRandom() {
        if(Objects.isNull(random)) {
            random = new Random();
        }
        return random;
    }
}
