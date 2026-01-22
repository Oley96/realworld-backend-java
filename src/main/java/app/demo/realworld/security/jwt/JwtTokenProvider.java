package app.demo.realworld.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.audience}")
    private String audienceKey;

    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String issueToken(String subject) {
        Instant now = Instant.now();
        Instant validity = now.plus(Duration.ofMinutes(15));

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(validity))
                .setAudience(audienceKey)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token is not valid: {}", e.getMessage());
            return false;
        }
    }

    public Jws<Claims> getClaims(String token) {
        var parsedToken = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token);

        var claims = parsedToken.getBody();

        if (claims.getIssuedAt() == null || claims.getIssuedAt().after(new Date())) {
            throw new JwtException("Invalid issued at (iat) claim");
        }
        if (claims.getSubject() == null || claims.getSubject().isEmpty()) {
            throw new JwtException("Invalid subject (sub) claim");
        }
        if (!audienceKey.equals(claims.getAudience())) {
            throw new JwtException("Invalid audience (aud) claim");
        }
        return parsedToken;
    }

    public String getSubject(String token) {
        return getClaims(token).getBody().getSubject();
    }
}
