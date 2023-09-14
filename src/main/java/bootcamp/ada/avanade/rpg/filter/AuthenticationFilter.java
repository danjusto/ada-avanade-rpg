package bootcamp.ada.avanade.rpg.filter;

import bootcamp.ada.avanade.rpg.services.Authentication;
import bootcamp.ada.avanade.rpg.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private Authentication authentication;
    public AuthenticationFilter(JwtService jwtService, Authentication authentication) {
        this.jwtService = jwtService;
        this.authentication = authentication;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = getToken(request);
        if (Boolean.TRUE.equals(this.jwtService.validateToken(token))) {
            var subject = this.jwtService.getSubject(token);
            var user = this.authentication.loadUserByUsername(subject);
            var auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
    private String getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer", "").trim();
    }
}
