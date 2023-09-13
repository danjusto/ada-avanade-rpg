package bootcamp.ada.avanade.rpg.models.damage;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import bootcamp.ada.avanade.rpg.services.BattleService;
import bootcamp.ada.avanade.rpg.services.ShiftService;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
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
        confirmHit(shift);
        CharacterClass monsterStats = battle.getCharacterClass();
        int diceDamage = rollCustomDice(monsterStats.getDiceFaces(), monsterStats.getDiceQty());
        shift.updateMonsterDmgAndCharacterHP(diceDamage);
        if (shift.getPvCharacter() == 0) {
            endBattle(battle, shift, false);
            this.battleRepository.save(battle);
            return this.shiftRepository.save(shift).damageMonsterDTO();
        }
        endShift(battle, shift);
        this.battleRepository.save(battle);
        return this.shiftRepository.save(shift).damageMonsterDTO();
    }
    @Override
    protected void confirmHit(Shift shift) {
        if (Boolean.FALSE.equals(shift.getCharacterHit())) {
            throw new AppException("Monster missed attack");
        }
    }
    @Override
    protected void checkDuplicateDamage(Shift shift) {
        if (shift.getDamageMonster() != 0) {
            throw new AppException("Damage already registered");
        }
    }
    private void endShift(Battle battle, Shift shift) {
        shift.setActive(false);
        battle.nextShift();
    }
}
