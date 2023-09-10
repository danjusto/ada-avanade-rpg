package bootcamp.ada.avanade.rpg.services;

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
        checkUserWithEmailExists(dto.email());
        var user = new User(dto);
        user.setPassword(this.passwordEncoder.encode(dto.password()));
        var newRegisteredUser = this.userRepository.save(user);
        return newRegisteredUser.dto();
    }
    @Transactional
    public void executeChangePassword(Long id, PasswordRequestDTO dto) {
        var user = getUser(id);
        if (!this.passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new PasswordException("Password not match");
        }
        user.changePassword(dto.newPassword());
        userRepository.save(user);
    }
    @Transactional
    public UserResponseDTO executeEditUser(Long id, UserRequestDTO dto) {
        var user = getUser(id);
        if (!this.passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new PasswordException("Password not match");
        }
        user.editNameAndEmail(dto.name(), dto.email());
        var updatedUser = userRepository.save(user);
        return updatedUser.dto();
    }
    protected User getUser(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user.get();
    }
    private void checkUserWithEmailExists(String email) {
        Optional<User> checkUserExists = this.userRepository.findByEmail(email);
        if (checkUserExists.isPresent()) {
            throw new AppException("Email already in use");
        }
    }
}
