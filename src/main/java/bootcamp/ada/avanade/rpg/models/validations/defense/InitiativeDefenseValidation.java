package bootcamp.ada.avanade.rpg.models.validations.defense;

import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.models.Initiative;
import org.springframework.stereotype.Component;

@Component
public class InitiativeDefenseValidation implements ValidateDefense {
    @Override
    public void validate(Shift shift, Battle battle) {
        if (battle.getInitiative() == Initiative.HERO && shift.getDiceAtkCharacter() == 0) {
            throw new AppException("Hero win in initiative and must attack first");
        }
    }
}
