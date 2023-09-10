package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.exception.AppException;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CharacterService {
    private CharacterRepository characterRepository;
    private UserService userService;
    public CharacterService(CharacterRepository characterRepository, UserService userService) {
        this.characterRepository = characterRepository;
        this.userService = userService;
    }
    @Transactional
    public CharacterResponseDTO executeCreate(CharacterRequestDTO dto) {
        var user = this.userService.getUser(dto.userId());
        Optional<Character> checkCharacterNameExists = this.characterRepository.findByUserIdAndName(dto.userId(), dto.name());
        if (checkCharacterNameExists.isPresent()) {
            throw new AppException("You already have a character with this name");
        }
        var newCharacter = this.characterRepository.save(new Character(dto, user));
        return newCharacter.dto();
    }
    public CharacterResponseDTO executeDetails(Long id) {
        var character = getChar(id);
        return character.dto();
    }
    @Transactional
    public void executeRemove(Long id) {
        var character = getChar(id);
        this.characterRepository.delete(character);
    }
    @Transactional
    public CharacterResponseDTO executeChangeName(Long id, CharacterRequestDTO dto) {
        Optional<Character> checkCharacterNameExists = this.characterRepository.findByUserIdAndName(dto.userId(), dto.name());
        if (checkCharacterNameExists.isPresent()) {
            throw new AppException("You already have a character with this name");
        }
        var character = getChar(id);
        character.changeName(dto.name());
        var updatedCharacter = this.characterRepository.save(character);
        return updatedCharacter.dto();
    }
    protected Character getChar(Long id) {
        Optional<Character> characterOptional = this.characterRepository.findById(id);
        if (characterOptional.isEmpty()) {
            throw new EntityNotFoundException("Character not found");
        }
        return characterOptional.get();
    }
}
