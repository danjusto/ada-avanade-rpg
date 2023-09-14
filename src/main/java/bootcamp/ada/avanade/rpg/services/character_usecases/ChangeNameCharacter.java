package bootcamp.ada.avanade.rpg.services.character_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterEditRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterDetailsResponseDTO;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import bootcamp.ada.avanade.rpg.services.CharacterService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ChangeNameCharacter extends CharacterService {
    public ChangeNameCharacter(CharacterRepository characterRepository, UserRepository userRepository) {
        super(characterRepository, userRepository);
    }
    @Transactional
    public CharacterDetailsResponseDTO execute(Principal principal, Long id, CharacterEditRequestDTO dto) {
        var user = getUserByUsername(principal.getName());
        checkCharacterNameExists(user.getId(), dto.name());
        var character = getCharacter(id, user.getId());
        character.changeName(dto.name());
        return this.characterRepository.save(character).dto();
    }
}
