package bootcamp.ada.avanade.rpg.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    private static SecretKey jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public String generateToken(String username) {
        var now = LocalDateTime.now();
        var expiration = now.plusHours(12);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .setExpiration(new Date(expiration.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .signWith(jwtSecret)
                .compact();
    }
    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public Boolean validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .isSigned(token);
    }
}
