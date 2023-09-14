package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterDetailsResponseDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterListDTO;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public abstract class CharacterService {
    protected CharacterRepository characterRepository;
    protected UserRepository userRepository;
    protected CharacterService(CharacterRepository characterRepository, UserRepository userRepository) {
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
    }
    protected Character getCharacter(Long id, Long userId) {
        Optional<Character> characterOptional = this.characterRepository.findByIdAndUserId(id, userId);
        if (characterOptional.isEmpty()) {
            throw new EntityNotFoundException("Character not found");
        }
        return characterOptional.get();
    }
    protected User getUserByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user.get();
    }
    protected void checkCharacterNameExists(Long userId, String name) {
        Optional<Character> checkCharacterNameExists = this.characterRepository.findByUserIdAndName(userId, name);
        if (checkCharacterNameExists.isPresent()) {
            throw new AppException("You already have a character with this name");
        }
    }
}
