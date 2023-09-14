package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.BattleDTO;
import bootcamp.ada.avanade.rpg.dto.response.BattleDetailsDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BattleServiceTest {
    @InjectMocks
    private BattleService service;
    @Mock
    private BattleRepository battleRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private UserRepository userRepository;
    private User user;
    private Principal principal;
    private Optional<User> userOptional;
    private Character character;
    private Optional<Character> characterOptional;
    private Battle battle;
    private Optional<Battle> battleOptional;
    private BattleDTO battleDTO;
    private List<Shift> shifts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startBattleTester();
    }
    @Test
    void ShouldStartBattleWithoutError() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(characterOptional);
        when(battleRepository.save(any())).thenReturn(battle);
        var response = assertDoesNotThrow(()->service.executePlay(principal, anyLong()));
        verify(battleRepository, times(1))
                .save(any());
        assertNotNull(response);
        assertEquals(BattleDTO.class, response.getClass());
        assertEquals(MonsterClass.class, response.monster().getClass());
        assertEquals(Initiative.class, response.initiative().getClass());
        assertEquals("Konan", response.character().name());
    }
    @Test
    void ShouldThrowErrorBecauseUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.executePlay(principal, anyLong()));
        assertEquals("User not found", exception.getMessage());
        verify(characterRepository, never())
                .save(any());
        verify(characterRepository, never())
                .findByUserIdAndName(any(), any());
    }
    @Test
    void ShouldThrowErrorBecauseCharacterNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.executePlay(principal, anyLong()));
        assertEquals("Character not found", exception.getMessage());
        verify(characterRepository, never())
                .save(any());
    }
    @Test
    void ShouldReturnHistoricOfSingleBattle() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        var response = assertDoesNotThrow(()->service.executeHistoric(anyLong(), anyLong()));
        verify(battleRepository, times(1))
                .findByIdAndCharacterId(any(), any());
        assertNotNull(response);
        assertEquals(BattleDetailsDTO.class, response.getClass());
        assertEquals(MonsterClass.class, response.monsterClass().getClass());
        assertEquals(CharClass.class, response.characterClass().getClass());
        assertEquals("Konan", response.characterName());
        assertEquals(2, response.shifts().size());
        assertEquals(20, response.shifts().get(0).hpMonster());
    }
    @Test
    void ShouldThrowErrorBecauseBattleNotFound() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.executeHistoric(anyLong(), anyLong()));
        assertEquals("Battle not found", exception.getMessage());
        verify(battleRepository, times(1))
                .findByIdAndCharacterId(any(), any());
    }
    private void startBattleTester() {
        this.principal = () -> "tester";
        this.user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.userOptional = Optional.of(this.user);
        this.character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        this.characterOptional = Optional.of(this.character);
        this.battle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        this.battleOptional = Optional.of(battle);
        this.battleDTO = new BattleDTO(1L, battle.getMonster(), battle.getInitiative(), character.dto());
        var shift1 = new Shift();
        var shift2 = new Shift();
        shift1.setId(1L);
        shift1.setRound(1);
        shift1.initialize(battle,20,30);
        shift1.updateAtk(10,5,true);
        shift1.updateDef(7,7, false);
        shift1.updateCharacterDmgAndMonsterHP(10);
        shift2.setId(2L);
        shift2.setRound(2);
        shift2.initialize(battle,20,20);
        shift2.updateAtk(5,6,false);
        shift2.updateDef(9,2, true);
        shift2.updateCharacterDmgAndMonsterHP(10);
        this.shifts = List.of(shift1, shift2);
        battle.setShifts(shifts);
    }
}