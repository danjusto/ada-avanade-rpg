package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationTest {
    @InjectMocks
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        user.setPassword("12345678");
        var userOptional = Optional.of(user);
        MockitoAnnotations.openMocks(this);
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        var response = assertDoesNotThrow(()->authentication.loadUserByUsername(anyString()));
        assertNotNull(response);
        assertEquals("tester", response.getUsername());
    }
}