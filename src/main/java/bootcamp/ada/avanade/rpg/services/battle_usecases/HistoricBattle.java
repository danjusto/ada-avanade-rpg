package bootcamp.ada.avanade.rpg.services.battle_usecases;

import bootcamp.ada.avanade.rpg.dto.response.BattleDetailsDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HistoricBattle {
    private BattleRepository battleRepository;
    public HistoricBattle(BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }
    public BattleDetailsDTO execute(Long characterId, Long battleId) {
        Battle battle = getBattle(characterId, battleId);
        return new BattleDetailsDTO(
                battle.getId(),
                battle.getCharacter().getId(),
                battle.getCharacter().getCharacterClass(),
                battle.getCharacter().getName(),
                battle.getMonster(),
                battle.getInitiative(),
                battle.getShifts().stream().map(Shift::dto).toList()
        );
    }
    private Battle getBattle(Long characterId, Long id) {
        Optional<Battle> battle = this.battleRepository.findByIdAndCharacterId(id, characterId);
        if (battle.isEmpty()) {
            throw new EntityNotFoundException("Battle not found");
        }
        return battle.get();
    }
}
