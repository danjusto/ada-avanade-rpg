package bootcamp.ada.avanade.rpg.services.shift_usecases;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.DamageRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.DefenseDTO;
import bootcamp.ada.avanade.rpg.entities.Battle;
import bootcamp.ada.avanade.rpg.entities.Character;
import bootcamp.ada.avanade.rpg.entities.Shift;
import bootcamp.ada.avanade.rpg.entities.User;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import bootcamp.ada.avanade.rpg.models.validations.attack.ValidateAttack;
import bootcamp.ada.avanade.rpg.models.validations.defense.ValidateDefense;
import bootcamp.ada.avanade.rpg.repositories.BattleRepository;
import bootcamp.ada.avanade.rpg.repositories.ShiftRepository;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.HeroWithInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.HeroWithoutInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.MonsterWithInitiative;
import bootcamp.ada.avanade.rpg.services.shift_usecases.damage_strategies.MonsterWithoutInitiative;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class DefenseTest {
    @InjectMocks
    private Defense useCase;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private BattleRepository battleRepository;
    @Mock
    private List<ValidateDefense> validateDefense;
    private Optional<Battle> battleOptional;
    private Shift shiftFirstDamage;
    private Optional<Shift> shiftFirstDamageOptional;
    private Shift shiftSecondMove;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startShiftTester();
    }
    @Test
    void ShouldCompleteFlowWithoutException() {
        when(battleRepository.findByIdAndCharacterId(anyLong(), anyLong())).thenReturn(battleOptional);
        when(shiftRepository.findByBattleIdAndActiveTrue(anyLong())).thenReturn(shiftFirstDamageOptional);
        doNothing().when(validateDefense).forEach(any());
        when(shiftRepository.save(any())).thenReturn(shiftSecondMove);
        assertDoesNotThrow(()->useCase.execute(anyLong(), anyLong()));
        verify(shiftRepository, times(1))
                .save(any());
    }
    private void startShiftTester() {
        var user = new User(new UserRequestDTO("Tester", "tester", "tester@email.com", "12345678"));
        var character = new Character(new CharacterRequestDTO("Konan", CharClass.BARBARIAN), user);
        var battle = new Battle(MonsterClass.ORC, Initiative.HERO, character);
        this.battleOptional = Optional.of(battle);
        this.shiftFirstDamage = new Shift();
        shiftFirstDamage.initialize(battle, 30, 40);
        shiftFirstDamage.updateAtk(10,5,true);
        shiftFirstDamage.updateCharacterDmgAndMonsterHP(10);
        this.shiftFirstDamageOptional = Optional.of(shiftFirstDamage);
        this.shiftSecondMove = new Shift();
        shiftSecondMove.initialize(battle, 30, 40);
        shiftSecondMove.updateAtk(10,5,true);
        shiftSecondMove.updateCharacterDmgAndMonsterHP(10);
        shiftSecondMove.updateDef(7,7, true);
    }
}