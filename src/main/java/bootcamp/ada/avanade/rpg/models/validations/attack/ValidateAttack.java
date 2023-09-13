package bootcamp.ada.avanade.rpg.models.validations.attack;

import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;

public interface ValidateAttack {
    void validate(Shift shift, Battle battle);
}
