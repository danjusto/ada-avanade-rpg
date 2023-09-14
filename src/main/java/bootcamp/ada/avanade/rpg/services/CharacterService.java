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
public class CharacterService {
    private CharacterRepository characterRepository;
    private UserRepository userRepository;
    public CharacterService(CharacterRepository characterRepository, UserRepository userRepository) {
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public CharacterDetailsResponseDTO executeCreate(Principal principal, CharacterRequestDTO dto) {
        var user = getUserByUsername(principal.getName());
        checkCharacterNameExists(user.getId(), dto.name());
        return this.characterRepository.save(new Character(dto, user)).dto();
    }
    public Page<CharacterListDTO> executeList(Principal principal, Pageable pagination) {
        var user = getUserByUsername(principal.getName());
        return this.characterRepository.findAllByUserId(user.getId(), pagination).map(Character::dtoList);
    }
    public CharacterDetailsResponseDTO executeDetails(Principal principal, Long id) {
        var user = getUserByUsername(principal.getName());
        return getCharacter(id, user.getId()).dto();
    }
    @Transactional
    public void executeRemove(Principal principal, Long id) {
        var user = getUserByUsername(principal.getName());
        var character = getCharacter(id, user.getId());
        this.characterRepository.delete(character);
    }
    @Transactional
    public CharacterDetailsResponseDTO executeChangeName(Principal principal, Long id, CharacterRequestDTO dto) {
        var user = getUserByUsername(principal.getName());
        checkCharacterNameExists(user.getId(), dto.name());
        var character = getCharacter(id, user.getId());
        character.changeName(dto.name());
        return this.characterRepository.save(character).dto();
    }
    private Character getCharacter(Long id, Long userId) {
        Optional<Character> characterOptional = this.characterRepository.findByIdAndUserId(id, userId);
        if (characterOptional.isEmpty()) {
            throw new EntityNotFoundException("Character not found");
        }
        return characterOptional.get();
    }
    private User getUserByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user.get();
    }
    private void checkCharacterNameExists(Long userId, String name) {
        Optional<Character> checkCharacterNameExists = this.characterRepository.findByUserIdAndName(userId, name);
        if (checkCharacterNameExists.isPresent()) {
            throw new AppException("You already have a character with this name");
        }
    }
}
