package bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.PlayBookException;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import org.springframework.stereotype.Component;

@Component
public class MonsterWithInitiative extends MonsterDamage implements StrategyDamage {
    public MonsterWithInitiative(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        super(shiftRepository, battleRepository);
    }
    @Override
    public DamageResponseDTO execute(Battle battle, Shift shift) {
        checkDuplicateDamage(shift);
        confirmHit(battle, shift);
        CharacterClass monsterStats = battle.getCharacterClass();
        int diceDamage = rollCustomDice(monsterStats.getDiceFaces(), monsterStats.getDiceQty());
        shift.updateMonsterDmgAndCharacterHP(diceDamage);
        if (shift.getHpCharacter() == 0) {
            endBattle(battle, shift, false);
        }
        return saveBattleAndShift(battle, shift);
    }
    @Override
    protected void confirmHit(Battle battle, Shift shift) {
        if (Boolean.FALSE.equals(shift.getMonsterHit())) {
            throw new PlayBookException("Monster missed attack");
        }
    }
}
