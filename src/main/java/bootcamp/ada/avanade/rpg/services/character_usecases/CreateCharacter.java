package bootcamp.ada.avanade.rpg.services.character_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterDetailsResponseDTO;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import bootcamp.ada.avanade.rpg.services.CharacterService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CreateCharacter extends CharacterService {
    public CreateCharacter(CharacterRepository characterRepository, UserRepository userRepository) {
        super(characterRepository, userRepository);
    }
    @Transactional
    public CharacterDetailsResponseDTO execute(Principal principal, CharacterRequestDTO dto) {
        var user = getUserByUsername(principal.getName());
        checkCharacterNameExists(user.getId(), dto.name());
        return this.characterRepository.save(new Character(dto, user)).dto();
    }
}
