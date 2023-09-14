package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.EditUserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.PasswordRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.UserResponseDTO;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.exception.PasswordException;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public UserResponseDTO executeCreate(UserRequestDTO dto) {
        checkIfUsernameIsAvaible(dto.username());
        checkIfEmailIsAvaible(dto.email());
        var user = new User(dto);
        user.setPassword(this.passwordEncoder.encode(dto.password()));
        return this.userRepository.save(user).dto();
    }
    @Transactional
    public void executeChangePassword(Principal principal, PasswordRequestDTO dto) {
        var user = getUserByUsername(principal.getName());
        checkIfPasswordMatch(dto.password(), user.getPassword());
        user.setPassword(this.passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }
    @Transactional
    public UserResponseDTO executeEditUser(Principal principal, EditUserRequestDTO dto) {
        var user = getUserByUsername(principal.getName());
        checkIfEmailIsAvaibleForChange(dto.email(), user.getId());
        checkIfPasswordMatch(dto.password(), user.getPassword());
        user.editNameAndEmail(dto.name(), dto.email());
        return userRepository.save(user).dto();
    }
    private User getUserByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user.get();
    }
    private void checkIfEmailIsAvaible(String email) {
        Optional<User> checkUserExists = this.userRepository.findByEmail(email);
        if (checkUserExists.isPresent()) {
            throw new AppException("Email already in use");
        }
    }
    private void checkIfEmailIsAvaibleForChange(String email, Long id) {
        Optional<User> checkUserExists = this.userRepository.findByEmailAndIdNot(email, id);
        if (checkUserExists.isPresent()) {
            throw new AppException("Email already in use");
        }
    }
    private void checkIfUsernameIsAvaible(String username) {
        Optional<User> checkUserExists = this.userRepository.findByUsername(username);
        if (checkUserExists.isPresent()) {
            throw new AppException("Username already in use");
        }
    }
    private void checkIfPasswordMatch(String passwordTyped, String userPassword) {
        if (!this.passwordEncoder.matches(passwordTyped, userPassword)) {
            throw new PasswordException("Password not match");
        }
    }
}
