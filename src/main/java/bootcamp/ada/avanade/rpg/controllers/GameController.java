package bootcamp.ada.avanade.rpg.controllers;

import bootcamp.ada.avanade.rpg.dto.request.DamageRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.*;
import bootcamp.ada.avanade.rpg.services.BattleService;
import bootcamp.ada.avanade.rpg.services.ShiftService;
import jakarta.validation.Valid;
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
    public AttackDTO attack(@PathVariable Long characterId, @PathVariable Long battleId) {
        return this.shiftService.executeAttack(characterId, battleId);
    }
    @PostMapping("{characterId}/defense/{battleId}")
    @ResponseStatus(HttpStatus.OK)
    public DefenseDTO defense(@PathVariable Long characterId, @PathVariable Long battleId) {
        return this.shiftService.executeDefense(characterId, battleId);
    }
    @PostMapping("{characterId}/damage/{battleId}")
    @ResponseStatus(HttpStatus.OK)
    public DamageResponseDTO calculateDamage(@PathVariable Long characterId, @PathVariable Long battleId, @RequestBody @Valid DamageRequestDTO dto) {
        return this.shiftService.executeCalculateDamage(characterId, battleId, dto);
    }
    @GetMapping("{characterId}/historic/{battleId}")
    @ResponseStatus(HttpStatus.OK)
    public BattleDetailsDTO historic(@PathVariable Long characterId, @PathVariable Long battleId){
        return this.battleService.executeHistoric(characterId, battleId);
    }
}
