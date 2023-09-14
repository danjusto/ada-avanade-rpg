package bootcamp.ada.avanade.rpg.services.shift_usecases;

import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.models.Turn;
import bootcamp.ada.avanade.rpg.models.validations.attack.ValidateAttack;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import bootcamp.ada.avanade.rpg.services.ShiftService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Attack extends ShiftService {
    private final List<ValidateAttack> validateAttacks;
    public Attack(ShiftRepository shiftRepository, BattleRepository battleRepository, List<ValidateAttack> validateAttacks) {
        super(shiftRepository, battleRepository);
        this.validateAttacks = validateAttacks;
    }
    @Transactional
    public AttackDTO execute(Long characterId, Long battleId) {
        Battle battle = getBattleAndCheckIfEnded(characterId, battleId);
        Shift shift = getOrInitShift(battle);
        this.validateAttacks.forEach(v -> v.validate(shift, battle));
        int diceAtk = rollDiceTwelve();
        int diceDef = rollDiceTwelve();
        Boolean hit = checkHit(battle, Turn.ATK, diceAtk, diceDef);
        shift.updateAtk(diceAtk, diceDef, hit);
        return this.shiftRepository.save(shift).attackCharacterDTO();
    }
}
