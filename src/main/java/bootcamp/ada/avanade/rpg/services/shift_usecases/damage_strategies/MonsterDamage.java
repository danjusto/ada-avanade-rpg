package bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.ValidateActionException;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;

public abstract class MonsterDamage extends Damage{
    private final ShiftRepository shiftRepository;
    private final BattleRepository battleRepository;
    protected MonsterDamage(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        this.shiftRepository = shiftRepository;
        this.battleRepository = battleRepository;
    }
    @Override
    protected void checkDuplicateDamage(Shift shift) {
        if (shift.getDamageMonster() != 0) {
            throw new ValidateActionException("Damage already registered");
        }
    }
    @Override
    protected DamageResponseDTO saveBattleAndShift(Battle battle, Shift shift) {
        this.battleRepository.save(battle);
        return this.shiftRepository.save(shift).damageMonsterDTO();
    }
}
