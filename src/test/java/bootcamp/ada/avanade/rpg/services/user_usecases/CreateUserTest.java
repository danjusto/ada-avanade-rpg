package bootcamp.ada.avanade.rpg.services.user_usecases;

import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CreateUserTest {
    @InjectMocks
    private CreateUser useCase;
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private User user;
    private Optional<User> userOptional;
    private UserRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUserTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(user);
        assertDoesNotThrow(()->useCase.execute(requestDTO));
        verify(repository, times(1))
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseUsernameAlreadyExist() {
        when(repository.findByUsername(anyString())).thenReturn(userOptional);
        AppException appException = assertThrows(AppException.class, () -> useCase.execute(requestDTO));
        assertEquals("Username already in use", appException.getMessage());
        verify(repository, never())
                .findByEmail(any());
        verify(repository, never())
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseEmailAlreadyExist() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(userOptional);
        AppException appException = assertThrows(AppException.class, () -> useCase.execute(requestDTO));
        assertEquals("Email already in use", appException.getMessage());
        verify(repository, never())
                .save(any());
    }
    private void startUserTester() {
        this.requestDTO = new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678");
        this.user = new User(this.requestDTO);
        this.user.setPassword("12345678");
        this.userOptional = Optional.of(this.user);
    }
}