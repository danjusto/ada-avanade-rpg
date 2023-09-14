package bootcamp.ada.avanade.rpg.services.character_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class RemoveCharacterTest {
    @InjectMocks
    private RemoveCharacter useCase;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private UserRepository userRepository;
    private Principal principal;
    private Optional<User> userOptional;
    private Optional<Character> characterOptional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCharacterTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(characterOptional);
        doNothing().when(characterRepository).delete(any());
        assertDoesNotThrow(()->useCase.execute(principal, any()));
        verify(characterRepository, times(1))
                .delete(any());
    }
    private void startCharacterTester() {
        this.principal = () -> "tester";
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.userOptional = Optional.of(user);
        var requestDTO = new CharacterRequestDTO("Konan", CharClass.BARBARIAN);
        var character = new Character(requestDTO, user);
        this.characterOptional = Optional.of(character);
    }
}