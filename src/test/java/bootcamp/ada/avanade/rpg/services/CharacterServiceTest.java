package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterResponseDTO;
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

class CharacterServiceTest {
    @InjectMocks
    private CharacterService service;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private UserRepository userRepository;
    private User user;
    private Principal principal;
    private Optional<User> userOptional;
    private Character character;
    private Optional<Character> characterOptional;
    private CharacterRequestDTO requestDTO;
    private CharacterRequestDTO requestNameDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCharacterTester();
    }
    @Test
    void ShouldReturnCharacterDtoAfterCreate() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByUserIdAndName(any(), anyString())).thenReturn(Optional.empty());
        when(characterRepository.save(any())).thenReturn(character);
        var response = assertDoesNotThrow(()->service.executeCreate(principal, requestDTO));
        verify(characterRepository, times(1))
                .save(any());
        assertNotNull(response);
        assertEquals(CharacterResponseDTO.class, response.getClass());
    }
    @Test
    void ShouldThrowErrorBecauseUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.executeCreate(principal, requestDTO));
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
        AppException appException = assertThrows(AppException.class, () -> service.executeCreate(principal, requestDTO));
        assertEquals("You already have a character with this name", appException.getMessage());
        verify(characterRepository, never())
                .save(any());
    }
    @Test
    void ShouldReturnSingleCharacter() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(characterOptional);
        var response = assertDoesNotThrow(()->service.executeDetails(principal, anyLong()));
        verify(characterRepository, times(1))
                .findByIdAndUserId(any(), any());
        assertNotNull(response);
        assertEquals(CharacterResponseDTO.class, response.getClass());
    }
    @Test
    void ShouldThrowErrorBecauseCharacterNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.executeDetails(principal, anyLong()));
        assertEquals("Character not found", exception.getMessage());
        verify(characterRepository, times(1))
                .findByIdAndUserId(any(), any());
    }
    @Test
    void ShouldRemoveCharacterWithoutError() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(characterOptional);
        doNothing().when(characterRepository).delete(any());
        assertDoesNotThrow(()->service.executeRemove(principal, any()));
        verify(characterRepository, times(1))
                .delete(any());
    }
    @Test
    void ShouldChangeNameOfCharacterAndReturnDto() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findByUserIdAndName(any(), anyString())).thenReturn(Optional.empty());
        when(characterRepository.findByIdAndUserId(any(), any())).thenReturn(characterOptional);
        when(characterRepository.save(any())).thenReturn(character);
        var response = assertDoesNotThrow(()->service.executeChangeName(principal, anyLong(), requestNameDTO));
        verify(characterRepository, times(1))
                .save(any());
        assertNotNull(response);
        assertEquals(CharacterResponseDTO.class, response.getClass());
        assertEquals("Xoviwas", response.name());
    }
    private void startCharacterTester() {
        this.principal = () -> "tester";
        this.user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.userOptional = Optional.of(this.user);
        this.requestDTO = new CharacterRequestDTO("Konan", CharClass.BARBARIAN);
        this.requestNameDTO = new CharacterRequestDTO("Xoviwas", CharClass.BARBARIAN);
        this.character = new Character(requestDTO, user);
        this.characterOptional = Optional.of(this.character);
    }
}