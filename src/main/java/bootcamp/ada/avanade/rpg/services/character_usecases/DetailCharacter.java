package bootcamp.ada.avanade.rpg.services.character_usecases;

import bootcamp.ada.avanade.rpg.dto.response.CharacterDetailsResponseDTO;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import bootcamp.ada.avanade.rpg.services.CharacterService;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class DetailCharacter extends CharacterService {
    public DetailCharacter(CharacterRepository characterRepository, UserRepository userRepository) {
        super(characterRepository, userRepository);
    }
    public CharacterDetailsResponseDTO execute(Principal principal, Long id) {
        var user = getUserByUsername(principal.getName());
        return getCharacter(id, user.getId()).dto();
    }
}
