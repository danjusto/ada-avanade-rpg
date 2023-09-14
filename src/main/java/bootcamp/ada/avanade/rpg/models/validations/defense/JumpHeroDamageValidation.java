package bootcamp.ada.avanade.rpg.models.validations.defense;

import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.ValidateActionException;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.validations.attack.ValidateAttack;
import org.springframework.stereotype.Component;

@Component
public class JumpHeroDamageValidation implements ValidateDefense {
    @Override
    public void validate(Shift shift, Battle battle) {
        if (battle.getInitiative() == Initiative.HERO && shift.getCharacterHit() == null) {
            throw new ValidateActionException("Must access damage endpoint first");
        }
    }
}
