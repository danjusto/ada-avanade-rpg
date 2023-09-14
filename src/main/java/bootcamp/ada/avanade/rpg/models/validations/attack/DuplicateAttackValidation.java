package bootcamp.ada.avanade.rpg.models.validations.attack;

import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.ValidateActionException;
import org.springframework.stereotype.Component;

@Component
public class DuplicateAttackValidation implements ValidateAttack {
    @Override
    public void validate(Shift shift, Battle battle) {
        if (shift.getDiceAtkCharacter() != 0) {
            throw new ValidateActionException("Hero already attacked this round");
        }
    }
}
