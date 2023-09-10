package bootcamp.ada.avanade.rpg.controllers;

import bootcamp.ada.avanade.rpg.dto.request.PasswordRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.UserResponseDTO;
import bootcamp.ada.avanade.rpg.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@RequestBody @Valid UserRequestDTO dto) {
        return this.userService.executeCreate(dto);
    }
    @PatchMapping("/password/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editPassword(@PathVariable Long id, @RequestBody @Valid PasswordRequestDTO dto) {
        this.userService.executeEditPassword(id, dto);
    }
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO editUser(@PathVariable Long id, @RequestBody @Valid UserRequestDTO dto) {
        return this.userService.executeEditUser(id, dto);
    }
}
