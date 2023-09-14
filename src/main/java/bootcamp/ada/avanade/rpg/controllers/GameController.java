package bootcamp.ada.avanade.rpg.controllers;

import bootcamp.ada.avanade.rpg.dto.request.DamageRequestDTO;

import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import bootcamp.ada.avanade.rpg.dto.response.BattleDTO;
import bootcamp.ada.avanade.rpg.dto.response.BattleDetailsDTO;
import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.dto.response.DefenseDTO;
import bootcamp.ada.avanade.rpg.services.battle_usecases.HistoricBattle;
import bootcamp.ada.avanade.rpg.services.battle_usecases.PlayBattle;
import bootcamp.ada.avanade.rpg.services.shift_usecases.Attack;
import bootcamp.ada.avanade.rpg.services.shift_usecases.CalculateDamage;
import bootcamp.ada.avanade.rpg.services.shift_usecases.Defense;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/battle")
@SecurityRequirement(name = "bearer-key")
public class GameController {
    private PlayBattle playBattle;
    private HistoricBattle historicBattle;
    private Attack attack;
    private Defense defense;
    private CalculateDamage calculateDamage;
    public GameController(PlayBattle playBattle, HistoricBattle historicBattle, Attack attack, Defense defense, CalculateDamage calculateDamage) {
        this.playBattle = playBattle;
        this.historicBattle = historicBattle;
        this.attack = attack;
        this.calculateDamage = calculateDamage;
        this.defense = defense;
    }
    @PostMapping("{characterId}/play")
    @ResponseStatus(HttpStatus.OK)
    public BattleDTO play(Principal principal, @PathVariable Long characterId) {
        return this.playBattle.execute(principal, characterId);
    }
    @PostMapping("{characterId}/attack/{battleId}")
    @ResponseStatus(HttpStatus.OK)
    public AttackDTO attack(@PathVariable Long characterId, @PathVariable Long battleId) {
        return this.attack.execute(characterId, battleId);
    }
    @PostMapping("{characterId}/defense/{battleId}")
    @ResponseStatus(HttpStatus.OK)
    public DefenseDTO defense(@PathVariable Long characterId, @PathVariable Long battleId) {
        return this.defense.execute(characterId, battleId);
    }
    @PostMapping("{characterId}/damage/{battleId}")
    @ResponseStatus(HttpStatus.OK)
    public DamageResponseDTO calculateDamage(@PathVariable Long characterId, @PathVariable Long battleId, @RequestBody @Valid DamageRequestDTO dto) {
        return this.calculateDamage.execute(characterId, battleId, dto);
    }
    @GetMapping("{characterId}/historic/{battleId}")
    @ResponseStatus(HttpStatus.OK)
    public BattleDetailsDTO historic(@PathVariable Long characterId, @PathVariable Long battleId){
        return this.historicBattle.execute(characterId, battleId);
    }
}
