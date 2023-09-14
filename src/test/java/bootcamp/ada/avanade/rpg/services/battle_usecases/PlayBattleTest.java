package bootcamp.ada.avanade.rpg.services.battle_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.BattleDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class PlayBattleTest {
    @InjectMocks
    private PlayBattle useCase;
    @Mock
    private BattleRepository battleRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private UserRepository userRepository;
    private Principal principal;
    private Optional<User> userOptional;
    private Optional<Character> characterOptional;
    private Battle battle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startBattleTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(characterOptional);
        when(battleRepository.save(any())).thenReturn(battle);
        assertDoesNotThrow(()->useCase.execute(principal, anyLong()));
        verify(battleRepository, times(1))
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(principal, anyLong()));
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
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(principal, anyLong()));
        assertEquals("Character not found", exception.getMessage());
        verify(characterRepository, never())
                .save(any());
    }
    private void startBattleTester() {
        this.principal = () -> "tester";
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.userOptional = Optional.of(user);
        var character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        this.characterOptional = Optional.of(character);
        this.battle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
    }
}