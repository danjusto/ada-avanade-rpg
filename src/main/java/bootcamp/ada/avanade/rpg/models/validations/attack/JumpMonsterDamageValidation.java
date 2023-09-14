package bootcamp.ada.avanade.rpg.models.validations.attack;

import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.ValidateActionException;
import bootcamp.ada.avanade.rpg.models.Initiative;
import org.springframework.stereotype.Component;

@Component
public class JumpMonsterDamageValidation implements ValidateAttack{
    @Override
    public void validate(Shift shift, Battle battle) {
       if (battle.getInitiative() == Initiative.MONSTER && shift.getMonsterHit() == null) {
           throw new ValidateActionException("Must access damage endpoint first");
       }
    }
}
