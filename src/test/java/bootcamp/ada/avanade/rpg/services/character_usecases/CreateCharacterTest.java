package bootcamp.ada.avanade.rpg.services.character_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.never;

class CreateCharacterTest {
    @InjectMocks
    private CreateCharacter useCase;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private UserRepository userRepository;
    private Principal principal;
    private Optional<User> userOptional;
    private Character character;
    private Optional<Character> characterOptional;
    private CharacterRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCharacterTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByUserIdAndName(any(), anyString())).thenReturn(Optional.empty());
        when(characterRepository.save(any())).thenReturn(character);
        assertDoesNotThrow(()->useCase.execute(principal, requestDTO));
        verify(characterRepository, times(1))
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(principal, requestDTO));
        assertEquals("User not found", exception.getMessage());
        verify(characterRepository, never())
                .save(any());
        verify(characterRepository, never())
                .findByUserIdAndName(any(), any());
    }
    @Test
    void ShouldThrowErrorBecauseCharacterNameAlreadyExist() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByUserIdAndName(any(), anyString())).thenReturn(characterOptional);
        AppException appException = assertThrows(AppException.class, () -> useCase.execute(principal, requestDTO));
        assertEquals("You already have a character with this name", appException.getMessage());
        verify(characterRepository, never())
                .save(any());
    }
    private void startCharacterTester() {
        this.principal = () -> "tester";
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.userOptional = Optional.of(user);
        this.requestDTO = new CharacterRequestDTO("Konan", CharClass.BARBARIAN);
        this.character = new Character(requestDTO, user);
        this.characterOptional = Optional.of(this.character);
    }
}