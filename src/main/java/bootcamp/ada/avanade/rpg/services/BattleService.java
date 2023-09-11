package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.response.BattleDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class BattleService {
    private BattleRepository battleRepository;
    private CharacterService characterService;
    private UserService userService;
    private static Random random;
    public BattleService(BattleRepository battleRepository, CharacterService characterService, UserService userService) {
        this.battleRepository = battleRepository;
        this.characterService = characterService;
        this.userService = userService;
    }
    @Transactional
    public BattleDTO executePlay(Principal principal, Long characterId) {
        var user = this.userService.getUserByEmail(principal.getName());
        var character = this.characterService.getCharacter(characterId, user.getId());
        var monster = chooseRandomMonster();
        var initiative = decideInitiative();
        var battle = new Battle(monster, initiative, character);
        var savedBattle = this.battleRepository.save(battle);
        return savedBattle.dto(character.dto());
    }
    protected Battle getBattle(Long id, Long characterId) {
        Optional<Battle> battle = this.battleRepository.findByIdAndCharacterId(id, characterId);
        if (battle.isEmpty()) {
            throw new EntityNotFoundException("Battle not found");
        }
        return battle.get();
    }
    private MonsterClass chooseRandomMonster() {
        int randomNumber = random.nextInt(MonsterClass.values().length);
        return MonsterClass.values()[randomNumber];
    }
    private Initiative decideInitiative() {
        int randomNumber = random.nextInt(Initiative.values().length);
        return Initiative.values()[randomNumber];
    }
    private static Random getRandom() {
        if(Objects.isNull(random)) {
            random = new Random();
        }
        return random;
    }
}
