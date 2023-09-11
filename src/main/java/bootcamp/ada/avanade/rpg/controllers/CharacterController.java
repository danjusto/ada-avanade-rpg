package bootcamp.ada.avanade.rpg.controllers;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterResponseDTO;
import bootcamp.ada.avanade.rpg.services.CharacterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/character")
public class CharacterController {
    private CharacterService characterService;
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterResponseDTO create(Principal user, @RequestBody @Valid CharacterRequestDTO dto) {
        return this.characterService.executeCreate(user, dto);
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<CharacterResponseDTO> details(Principal user, @PageableDefault(sort={"id"}) Pageable pagination) {
        return this.characterService.executeList(user, pagination);
    }
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterResponseDTO details(Principal user, @PathVariable Long id) {
        return this.characterService.executeDetails(user, id);
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(Principal user, @PathVariable Long id) {
        this.characterService.executeRemove(user, id);
    }
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterResponseDTO changeName(Principal user, @PathVariable Long id, @RequestBody @Valid CharacterRequestDTO dto) {
        return this.characterService.executeChangeName(user, id, dto);
    }
}
