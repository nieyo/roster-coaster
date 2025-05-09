package com.github.nieyo.security;

import com.github.nieyo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey jwtSecretKey;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        // Decode the base64-encoded secret
        this.jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(User user) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 86400000L; // 1 day

        return Jwts.builder()
                .subject(user.email())
                .claim("id", user.id())
                .issuedAt(new Date(nowMillis))
                .expiration(new Date(expMillis))
                .signWith(jwtSecretKey, Jwts.SIG.HS512)
                .compact();

    }

    public boolean validateToken(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    private boolean isTokenExpired(String jwt) {
        final Date expiration = extractAllClaims(jwt).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
