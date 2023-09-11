package bootcamp.ada.avanade.rpg.controllers;

import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import bootcamp.ada.avanade.rpg.dto.response.BattleDTO;
import bootcamp.ada.avanade.rpg.services.BattleService;
import bootcamp.ada.avanade.rpg.services.ShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/battle")
public class GameController {
    private BattleService battleService;
    private ShiftService shiftService;
    public GameController(BattleService battleService, ShiftService shiftService) {
        this.battleService = battleService;
        this.shiftService = shiftService;
    }
    @PostMapping("{characterId}/play")
    @ResponseStatus(HttpStatus.OK)
    public BattleDTO play(Principal principal, @PathVariable Long characterId) {
        return this.battleService.executePlay(principal, characterId);
    }
    @PostMapping("{characterId}/attack/{battleId}")
    @ResponseStatus(HttpStatus.OK)
    public AttackDTO attack(Principal principal, @PathVariable Long characterId, @PathVariable Long battleId) {
        return this.shiftService.executeAttack(principal, characterId, battleId);
    }
}
