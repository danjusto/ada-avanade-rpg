package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.PasswordException;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class UserService {
    protected UserRepository userRepository;
    protected PasswordEncoder passwordEncoder;
    protected UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    protected User getUserByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user.get();
    }
    protected void checkIfPasswordMatch(String passwordTyped, String userPassword) {
        if (!this.passwordEncoder.matches(passwordTyped, userPassword)) {
            throw new PasswordException("Password not match");
        }
    }
}
