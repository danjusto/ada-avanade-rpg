package bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.ValidateActionException;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;

public abstract class MonsterDamage extends Damage{
    protected MonsterDamage(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        super(shiftRepository, battleRepository);
    }
    @Override
    protected void checkDuplicateDamage(Shift shift) {
        if (shift.getDamageMonster() != 0) {
            throw new ValidateActionException("Damage already registered");
        }
    }
}
