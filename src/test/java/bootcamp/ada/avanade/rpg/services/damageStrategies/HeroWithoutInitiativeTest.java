package bootcamp.ada.avanade.rpg.services.damageStrategies;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class HeroWithoutInitiativeTest {
    @InjectMocks
    private HeroWithInitiative strategy;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private BattleRepository battleRepository;
    private Battle battle;
    private Battle endedBattle;
    private Shift shiftMissAtk;
    private Shift shiftHit;
    private Shift shiftDamage;
    private Shift shiftFinish;
    private Shift endedShift;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startStrategyTester();
    }
    @Test
    void ShouldThrowExceptionForMissedAttack() {
        AppException exception = assertThrows(AppException.class, () -> strategy.execute(battle, shiftMissAtk));
        assertEquals("Character missed attack", exception.getMessage());
    }
    @Test
    void ShouldThrowExceptionForDuplicateDamage() {
        AppException exception = assertThrows(AppException.class, () -> strategy.execute(battle, shiftDamage));
        assertEquals("Damage already registered", exception.getMessage());
    }
    @Test
    void ShouldDamageMonsterAndEndShift() {
        when(battleRepository.save(any())).thenReturn(battle);
        when(shiftRepository.save(any())).thenReturn(endedShift);
        var response = assertDoesNotThrow(()->strategy.execute(battle, shiftHit));
        verify(shiftRepository, times(1))
                .save(any());
        verify(battleRepository, times(1))
                .save(any());
        assertNotNull(response);
        assertEquals(DamageResponseDTO.class, response.getClass());
    }
    @Test
    void ShouldDamageMonsterAndEndBattle() {
        when(battleRepository.save(any())).thenReturn(endedBattle);
        when(shiftRepository.save(any())).thenReturn(endedShift);
        var response = assertDoesNotThrow(()->strategy.execute(battle, shiftFinish));
        verify(shiftRepository, times(1))
                .save(any());
        verify(battleRepository, times(1))
                .save(any());
        assertNotNull(response);
        assertEquals(DamageResponseDTO.class, response.getClass());
    }

    private void startStrategyTester() {
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        var character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        this.battle = new Battle(MonsterClass.ORC, Initiative.MONSTER, character);
        this.endedBattle = new Battle(MonsterClass.ORC, Initiative.MONSTER, character);
        endedBattle.endBattle();
        this.shiftMissAtk = new Shift();
        shiftMissAtk.initialize(battle, 30, 40);
        shiftMissAtk.updateDef(10, 5, true);
        shiftMissAtk.updateMonsterDmgAndCharacterHP(10);
        shiftMissAtk.updateAtk(10,5,false);
        this.shiftHit = new Shift();
        shiftHit.initialize(battle, 30, 40);
        shiftHit.updateDef(10, 5, true);
        shiftHit.updateMonsterDmgAndCharacterHP(10);
        shiftHit.updateAtk(10,5,true);
        this.shiftDamage = new Shift();
        shiftDamage.initialize(battle, 30, 40);
        shiftDamage.updateDef(10, 5, true);
        shiftDamage.updateMonsterDmgAndCharacterHP(10);
        shiftDamage.updateAtk(10,5,true);
        shiftDamage.updateCharacterDmgAndMonsterHP(10);
        this.shiftFinish = new Shift();
        shiftFinish.initialize(battle, 30, 1);
        shiftFinish.updateDef(10, 5, true);
        shiftFinish.updateMonsterDmgAndCharacterHP(10);
        shiftFinish.updateAtk(10,5,true);
        this.endedShift = new Shift();
        endedShift.initialize(battle, 30, 40);
        endedShift.updateDef(10, 5, true);
        endedShift.updateMonsterDmgAndCharacterHP(10);
        endedShift.updateAtk(10,5,true);
        endedShift.updateCharacterDmgAndMonsterHP(10);
        endedShift.setActive(false);
    }
}