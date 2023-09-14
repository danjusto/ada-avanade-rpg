package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.response.BattleDTO;
import bootcamp.ada.avanade.rpg.dto.response.BattleDetailsDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.CharacterRepository;
import bootcamp.ada.avanade.rpg.repositories.UserRepository;
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
    private CharacterRepository characterRepository;
    private UserRepository userRepository;
    private static Random random;
    public BattleService(BattleRepository battleRepository, CharacterRepository characterRepository, UserRepository userRepository) {
        this.battleRepository = battleRepository;
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public BattleDTO executePlay(Principal principal, Long characterId) {
        User user = getUser(principal.getName());
        Character character = getCharacter(characterId, user.getId());
        MonsterClass monster = chooseRandomMonster();
        Initiative initiative = decideInitiative();
        Battle battle = new Battle(monster, initiative, character);
        return this.battleRepository.save(battle).dto(character.dto());
    }
    public BattleDetailsDTO executeHistoric(Long characterId, Long battleId) {
        Battle battle = getBattle(characterId, battleId);
        return new BattleDetailsDTO(
                battle.getId(),
                battle.getCharacter().getCharacterClass(),
                battle.getCharacter().getName(),
                battle.getMonster(),
                battle.getInitiative(),
                battle.getShifts().stream().map(Shift::dto).toList()
        );
    }
    private MonsterClass chooseRandomMonster() {
        int randomNumber = getRandom().nextInt(MonsterClass.values().length);
        return MonsterClass.values()[randomNumber];
    }
    private Initiative decideInitiative() {
        int diceCharacterInitiative = rollDiceTwenty();
        int diceMonsterInitiative = rollDiceTwenty();
        while (diceCharacterInitiative == diceMonsterInitiative) {
            diceCharacterInitiative = rollDiceTwenty();
            diceMonsterInitiative = rollDiceTwenty();
        }
        if (diceCharacterInitiative > diceMonsterInitiative) {
            return Initiative.HERO;
        }
        return Initiative.MONSTER;
    }
    private int rollDiceTwenty() {
        int min = 1;
        return getRandom().nextInt(20) + min;
    }
    private static Random getRandom() {
        if(Objects.isNull(random)) {
            random = new Random();
        }
        return random;
    }
    private Battle getBattle(Long characterId, Long id) {
        Optional<Battle> battle = this.battleRepository.findByIdAndCharacterId(id, characterId);
        if (battle.isEmpty()) {
            throw new EntityNotFoundException("Battle not found");
        }
        return battle.get();
    }
    private Character getCharacter(Long id, Long userId) {
        Optional<Character> characterOptional = this.characterRepository.findByIdAndUserId(id, userId);
        if (characterOptional.isEmpty()) {
            throw new EntityNotFoundException("Character not found");
        }
        return characterOptional.get();
    }
    private User getUser(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user.get();
    }
}
