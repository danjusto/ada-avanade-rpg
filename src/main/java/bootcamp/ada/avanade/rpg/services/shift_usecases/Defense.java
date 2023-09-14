package bootcamp.ada.avanade.rpg.services.shift_usecases;

import bootcamp.ada.avanade.rpg.dto.response.DefenseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.models.Turn;
import bootcamp.ada.avanade.rpg.models.validations.defense.ValidateDefense;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import bootcamp.ada.avanade.rpg.services.ShiftService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Defense extends ShiftService {
    private final List<ValidateDefense> validateDefense;
    public Defense(ShiftRepository shiftRepository, BattleRepository battleRepository, List<ValidateDefense> validateDefense) {
        super(shiftRepository, battleRepository);
        this.validateDefense = validateDefense;
    }
    @Transactional
    public DefenseDTO execute(Long characterId, Long battleId) {
        Battle battle = getBattleAndCheckIfEnded(characterId, battleId);
        Shift shift = getOrInitShift(battle);
        this.validateDefense.forEach(v -> v.validate(shift, battle));
        int diceAtk = rollDiceTwelve();
        int diceDef = rollDiceTwelve();
        shift.updateDef(diceAtk, diceDef);
        return this.shiftRepository.save(shift).defenseCharacterDTO();
    }
}
