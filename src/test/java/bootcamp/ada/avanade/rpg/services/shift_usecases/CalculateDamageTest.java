package bootcamp.ada.avanade.rpg.services.shift_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.DamageRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AlreadyEndedException;
import bootcamp.ada.avanade.rpg.exception.PlayBookException;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.HeroWithInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.MonsterWithoutInitiative;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculateDamageTest {
    @InjectMocks
    private CalculateDamage useCase;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private BattleRepository battleRepository;
    @Mock
    private HeroWithInitiative strategyHeroWithInitiative;
    @Mock
    private MonsterWithoutInitiative strategyMonsterWithoutInitiative;
    private Optional<Battle> battleOptional;
    private Shift shiftFirstMove;
    private Optional<Shift> shiftFirstMoveOptional;
    private Shift shiftFirstDamage;
    private Shift shiftSecondMove;
    private Optional<Shift> shiftSecondMoveOptional;
    private Shift shiftEnded;
    private DamageRequestDTO dmgRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startShiftTester();
    }
    @Test
    void ShouldThrowErrorBecauseShiftHasEnded() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        when(shiftRepository.findById(any())).thenReturn(shiftFirstMoveOptional);
        when(shiftRepository.findByIdAndActiveTrue(any())).thenReturn(Optional.empty());
        AlreadyEndedException exception = assertThrows(AlreadyEndedException.class, () -> useCase.execute(any(), any(), dmgRequestDto));
        assertEquals("Shift has ended", exception.getMessage());
    }
    @Test
    void ShouldThrowErrorBecauseShiftNotFound() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        when(shiftRepository.findById(any())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(any(), any(), dmgRequestDto));
        assertEquals("Shift not found", exception.getMessage());
    }
    @Test
    void ShouldCompleteFlowHeroInitiativeWithoutException() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        when(shiftRepository.findById(any())).thenReturn(shiftFirstMoveOptional);
        when(shiftRepository.findByIdAndActiveTrue(any())).thenReturn(shiftFirstMoveOptional);
        when(strategyHeroWithInitiative.execute(any(), any())).thenReturn(shiftFirstDamage.damageCharacterDTO());
        assertDoesNotThrow(()->useCase.execute(any(), any(), dmgRequestDto));
    }
    @Test
    void ShouldCompleteFlowMonsterInitiativeWithoutException() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        when(shiftRepository.findById(any())).thenReturn(shiftSecondMoveOptional);
        when(shiftRepository.findByIdAndActiveTrue(any())).thenReturn(shiftSecondMoveOptional);
        when(strategyMonsterWithoutInitiative.execute(any(), any())).thenReturn(shiftEnded.damageCharacterDTO());
        assertDoesNotThrow(()->useCase.execute(any(), any(), dmgRequestDto));
    }
    private void startShiftTester() {
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        var character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        var battle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        this.battleOptional = Optional.of(battle);
        this.shiftFirstMove = new Shift();
        shiftFirstMove.initialize(battle, 30, 40);
        shiftFirstMove.updateAtk(10,5);
        this.shiftFirstMoveOptional = Optional.of(shiftFirstMove);
        this.shiftFirstDamage = new Shift();
        shiftFirstDamage.initialize(battle, 30, 40);
        shiftFirstDamage.updateAtk(10,5);
        shiftFirstDamage.updateCharacterDmgAndMonsterHP(10);
        this.shiftSecondMove = new Shift();
        shiftSecondMove.initialize(battle, 30, 40);
        shiftSecondMove.updateAtk(10,5);
        shiftSecondMove.updateCharacterDmgAndMonsterHP(10);
        shiftSecondMove.updateDef(7,7);
        this.shiftSecondMoveOptional = Optional.of(shiftSecondMove);
        this.shiftEnded = new Shift();
        shiftEnded.initialize(battle, 30, 40);
        shiftEnded.updateAtk(10,5);
        shiftEnded.updateCharacterDmgAndMonsterHP(10);
        shiftEnded.updateDef(7,7);
        shiftEnded.updateMonsterDmgAndCharacterHP(10);
        shiftEnded.setActive(false);
        this.dmgRequestDto = new DamageRequestDTO(1L);
    }
}