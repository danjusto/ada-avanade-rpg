package bootcamp.ada.avanade.rpg.services.character_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterEditRequestDTO;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChangeNameCharacterTest {
    @InjectMocks
    ChangeNameCharacter useCase;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private UserRepository userRepository;
    private Principal principal;
    private Optional<User> userOptional;
    private Character character;
    private Optional<Character> characterOptional;
    private CharacterEditRequestDTO requestNameDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCharacterTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByUserIdAndName(any(), anyString())).thenReturn(Optional.empty());
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(characterOptional);
        when(characterRepository.save(any())).thenReturn(character);
        assertDoesNotThrow(()->useCase.execute(principal, anyLong(), requestNameDTO));
        verify(characterRepository, times(1))
                .save(any());
    }
    private void startCharacterTester() {
        this.principal = () -> "tester";
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.userOptional = Optional.of(user);
        var requestDTO = new CharacterRequestDTO("Konan", CharClass.BARBARIAN);
        this.requestNameDTO = new CharacterEditRequestDTO("Xoviwas");
        this.character = new Character(requestDTO, user);
        this.characterOptional = Optional.of(this.character);
    }
}