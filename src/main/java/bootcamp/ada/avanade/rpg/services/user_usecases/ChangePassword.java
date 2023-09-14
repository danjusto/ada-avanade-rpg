package bootcamp.ada.avanade.rpg.services.user_usecases;

import bootcamp.ada.avanade.rpg.dto.request.PasswordRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.UserResponseDTO;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import bootcamp.ada.avanade.rpg.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ChangePassword extends UserService {
    public ChangePassword(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(userRepository, passwordEncoder);
    }
    @Transactional
    public UserResponseDTO execute(Principal principal, PasswordRequestDTO dto) {
        var user = getUserByUsername(principal.getName());
        checkIfPasswordMatch(dto.password(), user.getPassword());
        user.setPassword(this.passwordEncoder.encode(dto.newPassword()));
        return userRepository.save(user).dto();
    }
}
