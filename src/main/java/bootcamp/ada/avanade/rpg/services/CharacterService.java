package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
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
            //erro
        }
        var newCharacter = this.characterRepository.save(new Character(dto, user));
        return newCharacter.dto();
    }
}
