package com.github.nieyo.security;

import com.github.nieyo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    private static final String SECRET = "cPNwZv901gohNwIQ9lFt7+42ajXVunTnRCbz0eb9ANUsw0ufUApVLTZHOKcGiqAr52lNljVB/0/PcJS3bzveoQ==";
    private JwtUtil jwtUtil;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
        testUser = new User(
                UUID.randomUUID(),
                "Alice",
                "alice@example.com",
                "password",
                List.of(),
                List.of()
        );
    }

    @Test
    void generateToken_shouldContainCorrectClaims() {
        String token = jwtUtil.generateToken(testUser);

        String subject = jwtUtil.extractUsername(token);
        assertThat(subject).isEqualTo(testUser.email());

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testUser.email());

        assertThat(jwtUtil.validateToken(token, userDetails)).isTrue();
    }

    @Test
    void validateToken_shouldReturnTrue_forValidToken() {
        String token = jwtUtil.generateToken(testUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testUser.email());

        assertThat(jwtUtil.validateToken(token, userDetails)).isTrue();
    }

    @Test
    void validateToken_shouldReturnFalse_forInvalidUsername() {
        String token = jwtUtil.generateToken(testUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("wrong@example.com");

        assertThat(jwtUtil.validateToken(token, userDetails)).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalse_forExpiredToken() {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis - 1000; // expired 1 second ago

        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .subject(testUser.email())
                .claim("id", testUser.id())
                .claim("name", testUser.name())
                .issuedAt(new java.util.Date(nowMillis - 2000))
                .expiration(new java.util.Date(expMillis))
                .signWith(
                        io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                io.jsonwebtoken.io.Decoders.BASE64.decode(SECRET)
                        ),
                        io.jsonwebtoken.Jwts.SIG.HS512
                )
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testUser.email());

        assertThat(jwtUtil.validateToken(expiredToken, userDetails)).isFalse();
    }

    @Test
    void extractUsername_shouldReturnSubject() {
        String token = jwtUtil.generateToken(testUser);
        assertThat(jwtUtil.extractUsername(token)).isEqualTo(testUser.email());
    }
}
