package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.PasswordRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.UserResponseDTO;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public UserResponseDTO executeCreate(UserRequestDTO dto) {
        Optional<User> checkUserExists = this.userRepository.findByEmail(dto.email());
        if (checkUserExists.isPresent()) {
            //erro
        }
        var newUser = this.userRepository.save(new User(dto));
        return newUser.dto();
    }
    @Transactional
    public void executeChangePassword(Long id, PasswordRequestDTO dto) {
        var user = getUser(id);
        if (dto.password() != user.getPassword()) {
            //erro
        }
        user.changePassword(dto.newPassword());
        userRepository.save(user);
    }
    @Transactional
    public UserResponseDTO executeEditUser(Long id, UserRequestDTO dto) {
        var user = getUser(id);
        if (dto.password() != user.getPassword()) {
            //erro
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
}
