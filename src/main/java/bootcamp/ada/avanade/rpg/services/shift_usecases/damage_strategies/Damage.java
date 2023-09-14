package bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.models.Turn;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;

import java.util.Objects;
import java.util.Random;

public abstract class Damage {
    protected static Random random;
    protected ShiftRepository shiftRepository;
    protected BattleRepository battleRepository;
    protected Damage(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        this.shiftRepository = shiftRepository;
        this.battleRepository = battleRepository;
    }
    protected abstract void checkDuplicateDamage(Shift shift);
    protected void saveBattleAndShift(Battle battle, Shift shift) {
        this.battleRepository.save(battle);
        this.shiftRepository.save(shift);
    }
    protected void endBattle(Battle battle, Shift shift, Boolean victorie) {
        battle.endBattle();
        battle.getCharacter().updateVictoriesAndDefeats(victorie);
        shift.setActive(false);
    }
    protected Boolean checkAndSetHit(Battle battle, Turn turn, Shift shift) {
        CharacterClass heroStats = battle.getCharacterClass();
        CharacterClass monsterStats = battle.getMonsterClass();
        if (turn == Turn.ATK) {
            var averageAtk = shift.getDiceAtkCharacter() + heroStats.getStrength() + heroStats.getAgility();
            var averageDef = shift.getDiceDefMonster() + monsterStats.getDefense() + monsterStats.getAgility();
            shift.setCharacterHit(averageAtk > averageDef);
            return shift.getCharacterHit();
        } else {
            var averageAtk = shift.getDiceAtkMonster() + monsterStats.getStrength() + monsterStats.getAgility();
            var averageDef = shift.getDiceDefCharacter() + heroStats.getDefense() + heroStats.getAgility();
            shift.setMonsterHit(averageAtk > averageDef);
            return shift.getMonsterHit();
        }
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
