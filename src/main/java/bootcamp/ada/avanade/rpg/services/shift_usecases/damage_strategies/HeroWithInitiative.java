package bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.PlayBookException;
import bootcamp.ada.avanade.rpg.exception.ValidateActionException;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import org.springframework.stereotype.Component;

@Component
public class HeroWithInitiative extends Damage implements StrategyDamage {
    private ShiftRepository shiftRepository;
    private BattleRepository battleRepository;
    public HeroWithInitiative(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        this.shiftRepository = shiftRepository;
        this.battleRepository = battleRepository;
    }
    @Override
    public DamageResponseDTO execute(Battle battle, Shift shift) {
        checkDuplicateDamage(shift);
        confirmHit(battle, shift);
        CharacterClass heroStats = battle.getCharacterClass();
        int diceDamage = rollCustomDice(heroStats.getDiceFaces(), heroStats.getDiceQty());
        shift.updateCharacterDmgAndMonsterHP(diceDamage);
        if (shift.getPvMonster() == 0) {
            endBattle(battle, shift, true);
        }
        return saveBattleAndShift(battle, shift);
    }
    @Override
    protected void confirmHit(Battle battle, Shift shift) {
        if (Boolean.FALSE.equals(shift.getCharacterHit())) {
            throw new PlayBookException("Character missed attack");
        }
    }
    @Override
    protected void checkDuplicateDamage(Shift shift) {
        if (shift.getDamageCharacter() != 0) {
            throw new ValidateActionException("Damage already registered");
        }
    }
    @Override
    protected DamageResponseDTO saveBattleAndShift(Battle battle, Shift shift) {
        this.battleRepository.save(battle);
        return this.shiftRepository.save(shift).damageCharacterDTO();
    }
}
