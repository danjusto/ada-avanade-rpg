package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.HealthPoints;
import bootcamp.ada.avanade.rpg.dto.request.DamageRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.dto.response.DefenseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.Turn;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.models.validations.attack.ValidateAttack;
import bootcamp.ada.avanade.rpg.models.validations.defense.ValidateDefense;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import bootcamp.ada.avanade.rpg.services.damageStrategies.HeroWithInitiative;
import bootcamp.ada.avanade.rpg.services.damageStrategies.HeroWithoutInitiative;
import bootcamp.ada.avanade.rpg.services.damageStrategies.MonsterWithInitiative;
import bootcamp.ada.avanade.rpg.services.damageStrategies.MonsterWithoutInitiative;
import bootcamp.ada.avanade.rpg.services.damageStrategies.StrategyDamage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class ShiftService {
    private ShiftRepository shiftRepository;
    private BattleRepository battleRepository;
    private List<ValidateAttack> validateAttacks;
    private List<ValidateDefense> validateDefense;
    private HeroWithInitiative strategyHeroWithInitiative;
    private HeroWithoutInitiative strategyHeroWithoutInitiative;
    private MonsterWithInitiative strategyMonsterWithInitiative;
    private MonsterWithoutInitiative strategyMonsterWithoutInitiative;
    private static Random random;
    public ShiftService(ShiftRepository shiftRepository, BattleRepository battleRepository, List<ValidateAttack> validateAttacks, List<ValidateDefense> validateDefense, HeroWithInitiative strategyHeroWithInitiative, HeroWithoutInitiative strategyHeroWithoutInitiative, MonsterWithInitiative strategyMonsterWithInitiative, MonsterWithoutInitiative strategyMonsterWithoutInitiative) {
        this.shiftRepository = shiftRepository;
        this.battleRepository = battleRepository;
        this.validateAttacks = validateAttacks;
        this.validateDefense = validateDefense;
        this.strategyHeroWithInitiative = strategyHeroWithInitiative;
        this.strategyHeroWithoutInitiative = strategyHeroWithoutInitiative;
        this.strategyMonsterWithInitiative = strategyMonsterWithInitiative;
        this.strategyMonsterWithoutInitiative = strategyMonsterWithoutInitiative;
    }
    @Transactional
    public AttackDTO executeAttack(Long characterId, Long battleId) {
        Battle battle = getBattleAndCheckIfEnded(characterId, battleId);
        Shift shift = getOrInitShift(battle);
        this.validateAttacks.forEach(v -> v.validate(shift, battle));
        int diceAtk = rollDiceTwelve();
        int diceDef = rollDiceTwelve();
        Boolean hit = checkHit(battle, Turn.ATK, diceAtk, diceDef);
        shift.updateAtk(diceAtk, diceDef, hit);
        return this.shiftRepository.save(shift).attackCharacterDTO();
    }
    @Transactional
    public DefenseDTO executeDefense(Long characterId, Long battleId) {
        Battle battle = getBattleAndCheckIfEnded(characterId, battleId);
        Shift shift = getOrInitShift(battle);
        this.validateDefense.forEach(v -> v.validate(shift, battle));
        int diceAtk = rollDiceTwelve();
        int diceDef = rollDiceTwelve();
        Boolean hit = checkHit(battle, Turn.DEF, diceAtk, diceDef);
        shift.updateDef(diceAtk, diceDef, hit);
        return this.shiftRepository.save(shift).defenseCharacterDTO();
    }
    @Transactional
    public DamageResponseDTO executeCalculateDamage(Long characterId, Long battleId, DamageRequestDTO dto) {
        var battle = getBattleAndCheckIfEnded(characterId, battleId);
        var shift = getShiftAndCheckIfEnded(dto.shiftId());
        StrategyDamage strategyDamage = chooseStrategy(battle, shift);
        return strategyDamage.execute(battle, shift);
    }
    private StrategyDamage chooseStrategy(Battle battle, Shift shift) {
        if (battle.getInitiative() == Initiative.HERO && shift.getDiceDefCharacter() == 0) {
            return this.strategyHeroWithInitiative;
        } else if (battle.getInitiative() == Initiative.MONSTER && shift.getDiceDefMonster() == 0) {
            return this.strategyMonsterWithInitiative;
        } else if (battle.getInitiative() == Initiative.MONSTER) {
            return this.strategyHeroWithoutInitiative;
        } else {
            return this.strategyMonsterWithoutInitiative;
        }
    }
    private Shift getShiftAndCheckIfEnded(Long id) {
        Optional<Shift> shiftOptional = this.shiftRepository.findByIdAndActiveTrue(id);
        if (shiftOptional.isEmpty()) {
            throw new AppException("Shift has ended");
        }
        return shiftOptional.get();
    }
    private Shift getOrInitShift(Battle battle) {
        var shiftOptional = this.shiftRepository.findByBattleIdAndActiveTrue(battle.getId());
        if (shiftOptional.isEmpty()) {
            var newShift = new Shift();
            var healthPoints = getHealthPointsOfLastShift(battle);
            newShift.initialize(battle, healthPoints.heroHP(), healthPoints.monsterHP());
            return newShift;
        }
        return shiftOptional.get();
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
    private Battle getBattleAndCheckIfEnded(Long characterId, Long battleId) {
        var battleOptional = this.battleRepository.findByIdAndCharacterId(battleId, characterId);
        if (battleOptional.isEmpty()) {
            throw new EntityNotFoundException("Battle not found");
        }
        if (Boolean.FALSE.equals(battleOptional.get().getActive())) {
            throw new AppException("This battle already ended");
        }
        return battleOptional.get();
    }
    private Boolean checkHit(Battle battle, Turn turn, int diceAtk, int diceDef) {
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
    private int rollDiceTwelve() {
        int min = 1;
        return getRandom().nextInt(12) + min;
    }
    private static Random getRandom() {
        if(Objects.isNull(random)) {
            random = new Random();
        }
        return random;
    }
}
