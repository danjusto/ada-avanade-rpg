package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.LoginDTO;
import bootcamp.ada.avanade.rpg.dto.response.TokenDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class Login {
    private AuthenticationManager authManager;
    private JwtService jwtService;
    public Login(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }
    public TokenDTO execute(LoginDTO dto) throws AuthenticationException {
        var authToken = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        this.authManager.authenticate(authToken);
        var token = jwtService.generateToken(dto.username());
        return new TokenDTO("Bearer", token);
    }
}
