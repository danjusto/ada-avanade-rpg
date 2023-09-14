package bootcamp.ada.avanade.rpg.services.user_usecases;

import bootcamp.ada.avanade.rpg.dto.request.EditUserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AlreadyInUseException;
import bootcamp.ada.avanade.rpg.exception.PlayBookException;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
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

class EditUserTest {
    @InjectMocks EditUser useCase;
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private User user;
    private Principal principal;
    private Optional<User> userOptional;
    private EditUserRequestDTO editRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUserTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(repository.findByUsername(anyString())).thenReturn(userOptional);
        when(repository.findByEmailAndIdNot(any(), any())).thenReturn(Optional.empty());
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(repository.save(any())).thenReturn(user);
        assertDoesNotThrow(()->useCase.execute(principal, editRequestDTO));
        verify(repository, times(1))
                .save(any());
    }
    @Test
    void ShouldThrowErrorBecauseEmailAlreadyExistOnEdit() {
        when(repository.findByUsername(anyString())).thenReturn(userOptional);
        when(repository.findByEmailAndIdNot(anyString(), any())).thenReturn(userOptional);
        AlreadyInUseException exception = assertThrows(AlreadyInUseException.class, () -> useCase.execute(principal, editRequestDTO));
        assertEquals("Email already in use", exception.getMessage());
        verify(repository, never())
                .save(any());
    }
    private void startUserTester() {
        this.editRequestDTO = new EditUserRequestDTO("Tester Edited", "tester.edited@email.com", "12345678");
        this.user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        this.user.setPassword("12345678");
        this.userOptional = Optional.of(this.user);
        this.principal = () -> "tester";
    }
}