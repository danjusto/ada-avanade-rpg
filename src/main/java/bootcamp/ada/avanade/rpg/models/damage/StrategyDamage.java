package bootcamp.ada.avanade.rpg.models.damage;

import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;

public interface StrategyDamage {
    DamageResponseDTO execute(Battle battle, Shift shift);
}
