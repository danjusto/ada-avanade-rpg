package bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.PlayBookException;
import bootcamp.ada.avanade.rpg.models.Turn;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import org.springframework.stereotype.Component;

@Component
public class HeroWithoutInitiative extends HeroDamage implements StrategyDamage {
    protected HeroWithoutInitiative(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        super(shiftRepository, battleRepository);
    }
    @Override
    public DamageResponseDTO execute(Battle battle, Shift shift) {
        checkDuplicateDamage(shift);
        if(Boolean.FALSE.equals(checkAndSetHit(battle, Turn.ATK, shift))) {
            endShift(battle, shift);
            saveBattleAndShift(battle, shift);
            return shift.damageCharacterDTO();
        }
        CharacterClass heroStats = battle.getCharacterClass();
        int diceDamage = rollCustomDice(heroStats.getDiceFaces(), heroStats.getDiceQty());
        shift.updateCharacterDmgAndMonsterHP(diceDamage);
        if (shift.getHpMonster() == 0) {
            endBattle(battle, shift, true);
            saveBattleAndShift(battle, shift);
            return shift.damageCharacterDTO();
        }
        endShift(battle, shift);
        saveBattleAndShift(battle, shift);
        return shift.damageCharacterDTO();
    }
    private void endShift(Battle battle, Shift shift) {
        shift.setActive(false);
        battle.nextShift();
    }
}
