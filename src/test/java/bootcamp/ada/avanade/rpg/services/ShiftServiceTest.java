package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.DamageRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.dto.response.DefenseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import bootcamp.ada.avanade.rpg.services.damageStrategies.HeroWithInitiative;
import bootcamp.ada.avanade.rpg.services.damageStrategies.HeroWithoutInitiative;
import bootcamp.ada.avanade.rpg.services.damageStrategies.MonsterWithInitiative;
import bootcamp.ada.avanade.rpg.services.damageStrategies.MonsterWithoutInitiative;
import bootcamp.ada.avanade.rpg.models.validations.attack.ValidateAttack;
import bootcamp.ada.avanade.rpg.models.validations.defense.ValidateDefense;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShiftServiceTest {
    @InjectMocks
    private ShiftService service;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private BattleRepository battleRepository;
    @Mock
    private List<ValidateAttack> validateAttacks;
    @Mock
    private List<ValidateDefense> validateDefense;
    @Mock
    private HeroWithInitiative strategyHeroWithInitiative;
    @Mock
    private HeroWithoutInitiative strategyHeroWithoutInitiative;
    @Mock
    private MonsterWithInitiative strategyMonsterWithInitiative;
    @Mock
    private MonsterWithoutInitiative strategyMonsterWithoutInitiative;
    private User user;
    private Character character;
    private Optional<Character> characterOptional;
    private Battle battle;
    private Optional<Battle> battleOptional;
    private Battle endedBattle;
    private Optional<Battle> endedBattleOptional;
    private Shift shiftInitialized;
    private Optional<Shift> shiftInitializedOptional;
    private Shift shiftFirstMove;
    private Optional<Shift> shiftFirstMoveOptional;
    private Shift shiftFirstDamage;
    private Optional<Shift> shiftFirstDamageOptional;
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
    void ShouldThrowErrorBecauseBattleNotFound() {
        when(battleRepository.findByIdAndCharacterId(anyLong(), anyLong())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.executeAttack(anyLong(), anyLong()));
        assertEquals("Battle not found", exception.getMessage());
        verify(shiftRepository, never())
                .findByBattleIdAndActiveTrue(any());
        verify(shiftRepository, never())
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseBattleHasEnded() {
        when(battleRepository.findByIdAndCharacterId(anyLong(), anyLong())).thenReturn(endedBattleOptional);
        AppException exception = assertThrows(AppException.class, () -> service.executeAttack(anyLong(), anyLong()));
        assertEquals("This battle already ended", exception.getMessage());
        verify(shiftRepository, never())
                .findByBattleIdAndActiveTrue(any());
        verify(shiftRepository, never())
                .save(any());
    }
    @Test
    void ShouldAttackWithoutError() {
        when(battleRepository.findByIdAndCharacterId(anyLong(), anyLong())).thenReturn(battleOptional);
        when(shiftRepository.findByBattleIdAndActiveTrue(anyLong())).thenReturn(shiftInitializedOptional);
        doNothing().when(validateAttacks).forEach(any());
        when(shiftRepository.save(any())).thenReturn(shiftFirstMove);
        var response = assertDoesNotThrow(()->service.executeAttack(anyLong(), anyLong()));
        verify(shiftRepository, times(1))
                .save(any());
        assertNotNull(response);
        assertEquals(AttackDTO.class, response.getClass());
    }
    @Test
    void ShouldDefendWithoutError() {
        when(battleRepository.findByIdAndCharacterId(anyLong(), anyLong())).thenReturn(battleOptional);
        when(shiftRepository.findByBattleIdAndActiveTrue(anyLong())).thenReturn(shiftFirstDamageOptional);
        doNothing().when(validateDefense).forEach(any());
        when(shiftRepository.save(any())).thenReturn(shiftSecondMove);
        var response = assertDoesNotThrow(()->service.executeDefense(anyLong(), anyLong()));
        verify(shiftRepository, times(1))
                .save(any());
        assertNotNull(response);
        assertEquals(DefenseDTO.class, response.getClass());
    }
    @Test
    void ShouldThrowErrorBecauseShiftHasEnded() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        when(shiftRepository.findByIdAndActiveTrue(any())).thenReturn(Optional.empty());
        AppException exception = assertThrows(AppException.class, () -> service.executeCalculateDamage(any(), any(), dmgRequestDto));
        assertEquals("Shift has ended", exception.getMessage());
    }
    @Test
    void ShouldDamageMonsterWithoutError() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        when(shiftRepository.findByIdAndActiveTrue(any())).thenReturn(shiftFirstMoveOptional);
        when(strategyHeroWithInitiative.execute(any(), any())).thenReturn(shiftFirstDamage.damageCharacterDTO());
        var response = assertDoesNotThrow(()->service.executeCalculateDamage(any(), any(), dmgRequestDto));
        assertNotNull(response);
        assertEquals(DamageResponseDTO.class, response.getClass());
    }
    @Test
    void ShouldDamageHeroWithoutError() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        when(shiftRepository.findByIdAndActiveTrue(any())).thenReturn(shiftSecondMoveOptional);
        when(strategyMonsterWithoutInitiative.execute(any(), any())).thenReturn(shiftEnded.damageCharacterDTO());
        var response = assertDoesNotThrow(()->service.executeCalculateDamage(any(), any(), dmgRequestDto));
        assertNotNull(response);
        assertEquals(DamageResponseDTO.class, response.getClass());
    }
    private void startShiftTester() {
        this.user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        this.characterOptional = Optional.of(this.character);
        this.battle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        this.battleOptional = Optional.of(this.battle);
        this.endedBattle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        endedBattle.setActive(false);
        this.endedBattleOptional = Optional.of(this.endedBattle);
        this.shiftInitialized = new Shift();
        shiftInitialized.initialize(battle, 30, 40);
        this.shiftInitializedOptional = Optional.of(shiftInitialized);
        this.shiftFirstMove = new Shift();
        shiftFirstMove.initialize(battle, 30, 40);
        shiftFirstMove.updateAtk(10,5,true);
        this.shiftFirstMoveOptional = Optional.of(shiftFirstMove);
        this.shiftFirstDamage = new Shift();
        shiftFirstDamage.initialize(battle, 30, 40);
        shiftFirstDamage.updateAtk(10,5,true);
        shiftFirstDamage.updateCharacterDmgAndMonsterHP(10);
        this.shiftFirstDamageOptional = Optional.of(shiftFirstDamage);
        this.shiftSecondMove = new Shift();
        shiftSecondMove.initialize(battle, 30, 40);
        shiftSecondMove.updateAtk(10,5,true);
        shiftSecondMove.updateCharacterDmgAndMonsterHP(10);
        shiftSecondMove.updateDef(7,7, true);
        this.shiftSecondMoveOptional = Optional.of(shiftSecondMove);
        this.shiftEnded = new Shift();
        shiftEnded.initialize(battle, 30, 40);
        shiftEnded.updateAtk(10,5,true);
        shiftEnded.updateCharacterDmgAndMonsterHP(10);
        shiftEnded.updateDef(7,7, true);
        shiftEnded.updateMonsterDmgAndCharacterHP(10);
        shiftEnded.setActive(false);
        this.dmgRequestDto = new DamageRequestDTO(1L);
    }
}