package bootcamp.ada.avanade.rpg.services.character_usecases;

import bootcamp.ada.avanade.rpg.dto.response.CharacterListDTO;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
import bootcamp.ada.avanade.rpg.services.CharacterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ListCharacters extends CharacterService {
    public ListCharacters(CharacterRepository characterRepository, UserRepository userRepository) {
        super(characterRepository, userRepository);
    }
    public Page<CharacterListDTO> execute(Principal principal, Pageable pagination) {
        var user = getUserByUsername(principal.getName());
        return this.characterRepository.findAllByUserId(user.getId(), pagination).map(Character::dtoList);
    }
}
