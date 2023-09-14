package bootcamp.ada.avanade.rpg.dto.response;

import bootcamp.ada.avanade.rpg.models.CharClass;

public record CharacterListDTO(Long id, String name, CharClass characterClass) {
}
