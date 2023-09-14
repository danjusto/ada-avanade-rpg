package bootcamp.ada.avanade.rpg.services.battle_usecases;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class HistoricBattleTest {
    @InjectMocks
    private HistoricBattle useCase;
    @Mock
    private BattleRepository battleRepository;
    private Optional<Battle> battleOptional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startBattleTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(battleOptional);
        assertDoesNotThrow(()->useCase.execute(anyLong(), anyLong()));
        verify(battleRepository, times(1))
                .findByIdAndCharacterId(any(), any());
    }
    @Test
    void ShouldThrowErrorBecauseBattleNotFound() {
        when(battleRepository.findByIdAndCharacterId(any(), any())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(anyLong(), anyLong()));
        assertEquals("Battle not found", exception.getMessage());
        verify(battleRepository, times(1))
                .findByIdAndCharacterId(any(), any());
    }
    private void startBattleTester() {
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        var character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        var battle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        var shiftEnded = new Shift();
        shiftEnded.initialize(battle, 30, 40);
        shiftEnded.updateAtk(10,5);
        shiftEnded.updateCharacterDmgAndMonsterHP(10);
        shiftEnded.updateDef(7,7);
        shiftEnded.updateMonsterDmgAndCharacterHP(10);
        shiftEnded.setActive(false);
        battle.setShifts(List.of(shiftEnded));
        this.battleOptional = Optional.of(battle);
    }
}