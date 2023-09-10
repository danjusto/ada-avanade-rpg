package bootcamp.ada.avanade.rpg.controllers;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterResponseDTO;
import bootcamp.ada.avanade.rpg.services.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/character")
public class CharacterController {
    private CharacterService characterService;
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterResponseDTO create(@RequestBody @Valid CharacterRequestDTO dto) {
        return this.characterService.executeCreate(dto);
    }
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterResponseDTO details(@PathVariable Long id) {
        return this.characterService.executeDetails(id);
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        this.characterService.executeRemove(id);
    }
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterResponseDTO changeName(@PathVariable Long id, @RequestBody @Valid CharacterRequestDTO dto) {
        return this.characterService.executeChangeName(id, dto);
    }
}
