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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ListCharactersTest {
    @InjectMocks
    private ListCharacters useCase;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private UserRepository userRepository;
    private Principal principal;
    private Optional<User> userOptional;
    private PageImpl<Character> page;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCharacterTester();
    }
    @Test
    void execute() {
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(characterRepository.findAllByUserId(any(), any())).thenReturn(page);
        assertDoesNotThrow(()->useCase.execute(principal, any(Pageable.class)));
    }
    private void startCharacterTester() {
        this.principal = () -> "tester";
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.userOptional = Optional.of(user);
        var requestDTO = new CharacterRequestDTO("Konan", CharClass.BARBARIAN);
        var character = new Character(requestDTO, user);
        this.page = new PageImpl<>(List.of(character));
    }
}