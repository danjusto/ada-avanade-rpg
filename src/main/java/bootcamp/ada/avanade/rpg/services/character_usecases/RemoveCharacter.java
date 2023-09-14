package bootcamp.ada.avanade.rpg.services.character_usecases;

import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import bootcamp.ada.avanade.rpg.services.CharacterService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class RemoveCharacter extends CharacterService {
    public RemoveCharacter(CharacterRepository characterRepository, UserRepository userRepository) {
        super(characterRepository, userRepository);
    }
    @Transactional
    public void execute(Principal principal, Long id) {
        var user = getUserByUsername(principal.getName());
        var character = getCharacter(id, user.getId());
        this.characterRepository.delete(character);
    }
}
