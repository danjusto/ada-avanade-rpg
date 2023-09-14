package bootcamp.ada.avanade.rpg.services.shift_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.DamageRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import bootcamp.ada.avanade.rpg.models.validations.attack.ValidateAttack;
import bootcamp.ada.avanade.rpg.models.validations.defense.ValidateDefense;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.HeroWithInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.HeroWithoutInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.MonsterWithInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.MonsterWithoutInitiative;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AttackTest {
    @InjectMocks
    private Attack useCase;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private BattleRepository battleRepository;
    @Mock
    private List<ValidateAttack> validateAttacks;
    private Optional<Battle> battleOptional;
    private Optional<Battle> endedBattleOptional;
    private Optional<Shift> shiftInitializedOptional;
    private Shift shiftFirstMove;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startShiftTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(battleRepository.findByIdAndCharacterId(anyLong(), anyLong())).thenReturn(battleOptional);
        when(shiftRepository.findByBattleIdAndActiveTrue(anyLong())).thenReturn(shiftInitializedOptional);
        doNothing().when(validateAttacks).forEach(any());
        when(shiftRepository.save(any())).thenReturn(shiftFirstMove);
        assertDoesNotThrow(()->useCase.execute(anyLong(), anyLong()));
        verify(shiftRepository, times(1))
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseBattleNotFound() {
        when(battleRepository.findByIdAndCharacterId(anyLong(), anyLong())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(anyLong(), anyLong()));
        assertEquals("Battle not found", exception.getMessage());
        verify(shiftRepository, never())
                .findByBattleIdAndActiveTrue(any());
        verify(shiftRepository, never())
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseBattleHasEnded() {
        when(battleRepository.findByIdAndCharacterId(anyLong(), anyLong())).thenReturn(endedBattleOptional);
        AppException exception = assertThrows(AppException.class, () -> useCase.execute(anyLong(), anyLong()));
        assertEquals("This battle already ended", exception.getMessage());
        verify(shiftRepository, never())
                .findByBattleIdAndActiveTrue(any());
        verify(shiftRepository, never())
                .save(any());
    }
    private void startShiftTester() {
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        var character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        var battle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        this.battleOptional = Optional.of(battle);
        var endedBattle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        endedBattle.setActive(false);
        this.endedBattleOptional = Optional.of(endedBattle);
        var shiftInitialized = new Shift();
        shiftInitialized.initialize(battle, 30, 40);
        this.shiftInitializedOptional = Optional.of(shiftInitialized);
        this.shiftFirstMove = new Shift();
        shiftFirstMove.initialize(battle, 30, 40);
        shiftFirstMove.updateAtk(10,5,true);
    }
}