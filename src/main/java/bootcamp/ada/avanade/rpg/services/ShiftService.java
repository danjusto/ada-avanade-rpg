package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.models.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;
import java.util.Random;

@Service
public class ShiftService {
    private ShiftRepository shiftRepository;
    private BattleService battleService;
    private static Random random;
    public ShiftService(ShiftRepository shiftRepository, BattleService battleService) {
        this.shiftRepository = shiftRepository;
        this.battleService = battleService;
    }
    @Transactional
    public AttackDTO executeAttack(Principal principal, Long characterId, Long battleId) {
        var battle = this.battleService.getBattle(battleId, characterId);
        CharacterClass hero = battle.getCharacter().getCharacterClass().getInstanceOfCharacterClass();
        CharacterClass monster = battle.getMonster().getEnumCharClass().getInstanceOfCharacterClass();
        var shift = new Shift(battle);
        shift.setDiceAtkCharacter(rollDiceTwelve());
        shift.setDiceDefMonster(rollDiceTwelve());
        shift.setAvgAtkCharacter(shift.getDiceAtkCharacter() + hero.getStrength() + hero.getAgility());
        shift.setAvgDefMonster(shift.getDiceDefMonster() + hero.getDefense() + hero.getAgility());
        return shift.attackCharacterDTO();
    }
    private int rollCustomDice(int faces, int numberDices) {
        int result = 0;
        int min = 1;
        for (int i = 0; i < numberDices; i++) {
            var shot = random.nextInt(faces * numberDices) + min;
            result += shot;
        }
        return result;
    }
    private int rollDiceTwelve() {
        int min = 1;
        return random.nextInt(12) + min;
    }
    private int rollDiceTwenty() {
        int min = 1;
        return random.nextInt(20) + min;
    }
    private static Random getRandom() {
        if(Objects.isNull(random)) {
            random = new Random();
        }
        return random;
    }
}
