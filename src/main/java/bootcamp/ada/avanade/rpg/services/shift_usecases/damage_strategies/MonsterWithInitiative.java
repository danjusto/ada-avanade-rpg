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
public class MonsterWithInitiative extends MonsterDamage implements StrategyDamage {
    protected MonsterWithInitiative(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        super(shiftRepository, battleRepository);
    }
    @Override
    public DamageResponseDTO execute(Battle battle, Shift shift) {
        checkDuplicateDamage(shift);
        if(Boolean.FALSE.equals(checkAndSetHit(battle, Turn.DEF, shift))) {
            saveBattleAndShift(battle, shift);
            return shift.damageMonsterDTO();
        }
        CharacterClass monsterStats = battle.getCharacterClass();
        int diceDamage = rollCustomDice(monsterStats.getDiceFaces(), monsterStats.getDiceQty());
        shift.updateMonsterDmgAndCharacterHP(diceDamage);
        if (shift.getHpCharacter() == 0) {
            endBattle(battle, shift, false);
        }
        saveBattleAndShift(battle, shift);
        return shift.damageMonsterDTO();
    }
}
