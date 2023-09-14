package bootcamp.ada.avanade.rpg.services.shift_usecases;

import bootcamp.ada.avanade.rpg.dto.request.DamageRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.exception.AlreadyEndedException;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.Turn;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import bootcamp.ada.avanade.rpg.services.ShiftService;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.HeroWithInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.HeroWithoutInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.MonsterWithInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.MonsterWithoutInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.StrategyDamage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalculateDamage extends ShiftService {
    private final HeroWithInitiative strategyHeroWithInitiative;
    private final HeroWithoutInitiative strategyHeroWithoutInitiative;
    private final MonsterWithInitiative strategyMonsterWithInitiative;
    private final MonsterWithoutInitiative strategyMonsterWithoutInitiative;
    public CalculateDamage(ShiftRepository shiftRepository, BattleRepository battleRepository, HeroWithInitiative strategyHeroWithInitiative, HeroWithoutInitiative strategyHeroWithoutInitiative, MonsterWithInitiative strategyMonsterWithInitiative, MonsterWithoutInitiative strategyMonsterWithoutInitiative) {
        super(shiftRepository, battleRepository);
        this.strategyHeroWithInitiative = strategyHeroWithInitiative;
        this.strategyHeroWithoutInitiative = strategyHeroWithoutInitiative;
        this.strategyMonsterWithInitiative = strategyMonsterWithInitiative;
        this.strategyMonsterWithoutInitiative = strategyMonsterWithoutInitiative;
    }
    @Transactional
    public DamageResponseDTO execute(Long characterId, Long battleId, DamageRequestDTO dto) {
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
        Optional<Shift> firstTry = this.shiftRepository.findById(id);
        if (firstTry.isEmpty()) {
            throw new EntityNotFoundException("Shift not found");
        }
        Optional<Shift> secondTry = this.shiftRepository.findByIdAndActiveTrue(id);
        if (secondTry.isEmpty()) {
            throw new AlreadyEndedException("Shift has ended");
        }
        return secondTry.get();
    }
}
