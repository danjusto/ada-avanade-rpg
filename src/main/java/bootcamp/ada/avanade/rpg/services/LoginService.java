package bootcamp.ada.avanade.rpg.services;

import bootcamp.ada.avanade.rpg.dto.request.LoginDTO;
import bootcamp.ada.avanade.rpg.dto.response.TokenDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private AuthenticationManager authManager;
    private JwtService jwtService;
    public LoginService(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }
    public TokenDTO execute(LoginDTO dto) throws AuthenticationException {
        var authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        this.authManager.authenticate(authToken);
        var token = jwtService.generateToken(dto.email());
        return new TokenDTO("Bearer", token);
    }
}
