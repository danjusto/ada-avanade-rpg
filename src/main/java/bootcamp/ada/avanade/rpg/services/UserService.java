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
        if (checkUserExists.isEmpty()) {
            //erro
        }
        var newUser = this.userRepository.save(new User(dto));
        return newUser.dto();
    }
    @Transactional
    public void executeEditPassword(Long id, PasswordRequestDTO dto) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        if (dto.password() != user.get().getPassword()) {
            //erro
        }
        user.get().setPassword(dto.newPassword());
        userRepository.save(user.get());
    }
    @Transactional
    public UserResponseDTO executeEditUser(Long id, UserRequestDTO dto) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        if (dto.password() != user.get().getPassword()) {
            //erro
        }
        user.get().editNameAndEmail(dto.name(), dto.email());
        var updatedUser = userRepository.save(user.get());
        return updatedUser.dto();
    }
}
