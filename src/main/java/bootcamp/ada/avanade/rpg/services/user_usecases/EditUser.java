package bootcamp.ada.avanade.rpg.services.user_usecases;

import bootcamp.ada.avanade.rpg.dto.request.EditUserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.UserResponseDTO;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AlreadyInUseException;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import bootcamp.ada.avanade.rpg.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class EditUser extends UserService {
    public EditUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder);
    }
    @Transactional
    public UserResponseDTO execute(Principal principal, EditUserRequestDTO dto) {
        var user = getUserByUsername(principal.getName());
        checkIfEmailIsAvaibleForChange(dto.email(), user.getId());
        checkIfPasswordMatch(dto.password(), user.getPassword());
        user.editNameAndEmail(dto.name(), dto.email());
        return userRepository.save(user).dto();
    }
    private void checkIfEmailIsAvaibleForChange(String email, Long id) {
        Optional<User> checkUserExists = this.userRepository.findByEmailAndIdNot(email, id);
        if (checkUserExists.isPresent()) {
            throw new AlreadyInUseException("Email already in use");
        }
    }
}
