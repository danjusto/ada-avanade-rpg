package bootcamp.ada.avanade.rpg.services.damageStrategies;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import org.springframework.stereotype.Component;

@Component
public class MonsterWithoutInitiative extends Damage implements StrategyDamage {
    private ShiftRepository shiftRepository;
    private BattleRepository battleRepository;
    public MonsterWithoutInitiative(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        this.shiftRepository = shiftRepository;
        this.battleRepository = battleRepository;
    }
    @Override
    public DamageResponseDTO execute(Battle battle, Shift shift) {
        checkDuplicateDamage(shift);
        confirmHit(battle, shift);
        CharacterClass monsterStats = battle.getCharacterClass();
        int diceDamage = rollCustomDice(monsterStats.getDiceFaces(), monsterStats.getDiceQty());
        shift.updateMonsterDmgAndCharacterHP(diceDamage);
        if (shift.getPvCharacter() == 0) {
            endBattle(battle, shift, false);
            return saveBattleAndShift(battle, shift);
        }
        endShift(battle, shift);
        return saveBattleAndShift(battle, shift);
    }
    @Override
    protected void confirmHit(Battle battle, Shift shift) {
        if (Boolean.FALSE.equals(shift.getMonsterHit())) {
            endShift(battle, shift);
            throw new AppException("Monster missed attack");
        }
    }
    @Override
    protected void checkDuplicateDamage(Shift shift) {
        if (shift.getDamageMonster() != 0) {
            throw new AppException("Damage already registered");
        }
    }
    @Override
    protected DamageResponseDTO saveBattleAndShift(Battle battle, Shift shift) {
        this.battleRepository.save(battle);
        return this.shiftRepository.save(shift).damageMonsterDTO();
    }
    private void endShift(Battle battle, Shift shift) {
        shift.setActive(false);
        battle.nextShift();
    }
}
