package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.HealthPoints;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.AlreadyEndedException;
import bootcamp.ada.avanade.rpg.models.Turn;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
public abstract class ShiftService {
    protected ShiftRepository shiftRepository;
    protected BattleRepository battleRepository;
    private static Random random;
    protected ShiftService(ShiftRepository shiftRepository, BattleRepository battleRepository) {
        this.shiftRepository = shiftRepository;
        this.battleRepository = battleRepository;
    }
    protected Shift getOrInitShift(Battle battle) {
        var shiftOptional = this.shiftRepository.findByBattleIdAndActiveTrue(battle.getId());
        if (shiftOptional.isEmpty()) {
            var newShift = new Shift();
            var healthPoints = getHealthPointsOfLastShift(battle);
            newShift.initialize(battle, healthPoints.heroHP(), healthPoints.monsterHP());
            return newShift;
        }
        return shiftOptional.get();
    }
    protected Battle getBattleAndCheckIfEnded(Long characterId, Long battleId) {
        var battleOptional = this.battleRepository.findByIdAndCharacterId(battleId, characterId);
        if (battleOptional.isEmpty()) {
            throw new EntityNotFoundException("Battle not found");
        }
        if (Boolean.FALSE.equals(battleOptional.get().getActive())) {
            throw new AlreadyEndedException("This battle already ended");
        }
        return battleOptional.get();
    }
    protected Boolean checkHit(Battle battle, Turn turn, int diceAtk, int diceDef) {
        CharacterClass heroStats = battle.getCharacterClass();
        CharacterClass monsterStats = battle.getMonsterClass();
        int averageAtk;
        int averageDef;
        if (turn == Turn.ATK) {
            averageAtk = diceAtk + heroStats.getStrength() + heroStats.getAgility();
            averageDef = diceDef + monsterStats.getDefense() + monsterStats.getAgility();
        } else {
            averageAtk = diceAtk + monsterStats.getStrength() + monsterStats.getAgility();
            averageDef = diceDef + heroStats.getDefense() + heroStats.getAgility();
        }
        return averageAtk > averageDef;
    }
    protected int rollDiceTwelve() {
        int min = 1;
        return getRandom().nextInt(12) + min;
    }
    private HealthPoints getHealthPointsOfLastShift(Battle battle) {
        var shiftOptional = this.shiftRepository.findFirstByBattleIdAndActiveFalseOrderByIdDesc(battle.getId());
        if (shiftOptional.isEmpty()) {
            CharacterClass hero = battle.getCharacterClass();
            CharacterClass monster = battle.getMonsterClass();
            return new HealthPoints(hero.getHealthPoints(), monster.getHealthPoints());
        }
        return new HealthPoints(shiftOptional.get().getPvCharacter(), shiftOptional.get().getPvMonster());
    }
    private static Random getRandom() {
        if(Objects.isNull(random)) {
            random = new Random();
        }
        return random;
    }
}
