package bootcamp.ada.avanade.rpg.services.shift_usecases.damageStrategies;

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
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.HeroWithInitiative;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HeroWithInitiativeTest {
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
    void ShouldDamageMonster() {
        when(battleRepository.save(any())).thenReturn(battle);
        when(shiftRepository.save(any())).thenReturn(shiftDamage);
        var response = assertDoesNotThrow(()->strategy.execute(battle, shiftHit));
        verify(shiftRepository, times(1))
                .save(any());
        verify(battleRepository, times(1))
                .save(any());
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
    }
    private void startStrategyTester() {
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        var character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        this.battle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        this.endedBattle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        endedBattle.endBattle();
        this.shiftMissAtk = new Shift();
        shiftMissAtk.initialize(battle, 30, 40);
        shiftMissAtk.updateAtk(10,5,false);
        this.shiftHit = new Shift();
        shiftHit.initialize(battle, 30, 40);
        shiftHit.updateAtk(10,5,true);
        this.shiftDamage = new Shift();
        shiftDamage.initialize(battle, 30, 40);
        shiftDamage.updateAtk(10,5,true);
        shiftDamage.updateCharacterDmgAndMonsterHP(10);
        this.shiftFinish = new Shift();
        shiftFinish.initialize(battle, 30, 1);
        shiftFinish.updateAtk(10,5,true);
        this.endedShift = new Shift();
        endedShift.initialize(battle, 30, 8);
        endedShift.updateAtk(10,5,true);
        endedShift.updateCharacterDmgAndMonsterHP(10);
        endedShift.setActive(false);
    }
}