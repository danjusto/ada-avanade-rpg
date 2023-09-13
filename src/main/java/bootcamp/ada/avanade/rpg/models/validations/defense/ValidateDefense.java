package bootcamp.ada.avanade.rpg.models.validations.defense;

import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;

public interface ValidateDefense {
    void validate(Shift shift, Battle battle);
}
