package bootcamp.ada.avanade.rpg.controllers;

import bootcamp.ada.avanade.rpg.dto.request.EditUserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.PasswordRequestDTO;
import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.UserResponseDTO;
import bootcamp.ada.avanade.rpg.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import java.security.Principal;

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
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearer-key")
    public void changePassword(Principal principal, @RequestBody @Valid PasswordRequestDTO dto) {
        this.userService.executeChangePassword(principal, dto);
    }
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "bearer-key")
    public UserResponseDTO editUser(Principal principal, @RequestBody @Valid EditUserRequestDTO dto) {
        return this.userService.executeEditUser(principal, dto);
    }
}
