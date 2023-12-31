package bootcamp.ada.avanade.rpg.models.validations.defense;

import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.ValidateActionException;
import org.springframework.stereotype.Component;

@Component
public class DuplicateDefenseValidation implements ValidateDefense {
    @Override
    public void validate(Shift shift, Battle battle) {
        if (shift.getDiceDefCharacter() != 0) {
            throw new ValidateActionException("Hero already defended this round");
        }
    }
}
