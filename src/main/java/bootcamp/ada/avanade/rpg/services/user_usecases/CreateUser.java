package bootcamp.ada.avanade.rpg.services.user_usecases;

import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.UserResponseDTO;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AlreadyInUseException;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import bootcamp.ada.avanade.rpg.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUser extends UserService {
    public CreateUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder);
    }
    @Transactional
    public UserResponseDTO execute(UserRequestDTO dto) {
        checkIfUsernameIsAvaible(dto.username());
        checkIfEmailIsAvaible(dto.email());
        var user = new User(dto);
        user.setPassword(this.passwordEncoder.encode(dto.password()));
        return this.userRepository.save(user).dto();
    }
    private void checkIfEmailIsAvaible(String email) {
        Optional<User> checkUserExists = this.userRepository.findByEmail(email);
        if (checkUserExists.isPresent()) {
            throw new AlreadyInUseException("Email already in use");
        }
    }
    private void checkIfUsernameIsAvaible(String username) {
        Optional<User> checkUserExists = this.userRepository.findByUsername(username);
        if (checkUserExists.isPresent()) {
            throw new AlreadyInUseException("Username already in use");
        }
    }
}
