package bootcamp.ada.avanade.rpg.controllers;

import bootcamp.ada.avanade.rpg.dto.request.LoginDTO;
import bootcamp.ada.avanade.rpg.dto.response.TokenDTO;
import bootcamp.ada.avanade.rpg.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private LoginService loginService;
    public LoginController(LoginService service) {
        this.loginService = service;
    }
    @PostMapping
    public TokenDTO login(@RequestBody @Valid LoginDTO dto) {
        return this.loginService.execute(dto);
    }
}
