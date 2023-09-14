package bootcamp.ada.avanade.rpg.services.user_usecases;

import bootcamp.ada.avanade.rpg.dto.request.PasswordRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.PasswordException;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ChangePasswordTest {
    @InjectMocks
    private ChangePassword useCase;
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private User user;
    private Principal principal;
    private Optional<User> userOptional;
    private PasswordRequestDTO requestDTO;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUserTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(repository.findByUsername(anyString())).thenReturn(userOptional);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(repository.save(any())).thenReturn(user);
        assertDoesNotThrow(()->useCase.execute(principal, requestDTO));
        verify(repository, times(1))
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecausePasswordNotMatch() {
        when(repository.findByUsername(anyString())).thenReturn(userOptional);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        PasswordException exception = assertThrows(PasswordException.class, () -> useCase.execute(principal, requestDTO));
        assertEquals("Password not match", exception.getMessage());
        verify(repository, never())
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseUserNotFound() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(principal, requestDTO));
        assertEquals("User not found", exception.getMessage());
        verify(repository, never())
                .save(any());
    }
    private void startUserTester() {
        this.requestDTO = new PasswordRequestDTO("12345678", "14725836");
        this.user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.user.setPassword("12345678");
        this.userOptional = Optional.of(this.user);
        this.principal = () -> "tester";
    }
}