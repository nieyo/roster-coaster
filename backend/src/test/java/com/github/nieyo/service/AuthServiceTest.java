package com.github.nieyo.service;

import com.github.nieyo.dto.AuthResult;
import com.github.nieyo.dto.LoginRequest;
import com.github.nieyo.dto.RegisterRequest;
import com.github.nieyo.entity.User;
import com.github.nieyo.repository.UserRepository;
import com.github.nieyo.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IdService idService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_shouldSaveAndReturnUser_whenEmailNotInUse() {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password123");
        String hashedPassword = "hashedPassword";
        UUID userId = UUID.randomUUID();
        User savedUser = new User(userId, "Test User", "test@example.com", hashedPassword, List.of(), List.of());

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn(hashedPassword);
        when(idService.randomId()).thenReturn(userId);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.registerUser(request);

        assertThat(result).isEqualTo(savedUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrow_whenEmailAlreadyInUse() {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(mock(User.class)));

        assertThatThrownBy(() -> authService.registerUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    void registerUserAndGenerateToken_shouldReturnAuthResult() {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password123");
        UUID userId = UUID.randomUUID();
        String hashedPassword = "hashedPassword";
        User user = new User(userId, "Test User", "test@example.com", hashedPassword, List.of(), List.of());
        String token = "jwt-token";

        // Mock registerUser to return user
        AuthService spyService = Mockito.spy(authService);
        doReturn(user).when(spyService).registerUser(request);
        when(jwtUtil.generateToken(user)).thenReturn(token);

        AuthResult result = spyService.registerUserAndGenerateToken(request);

        assertThat(result.user()).isEqualTo(user);
        assertThat(result.token()).isEqualTo(token);
    }

    @Test
    void authenticateUserAndGenerateToken_shouldReturnAuthResult_whenCredentialsValid() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Test User", "test@example.com", "hashedPassword", List.of(), List.of());
        String token = "jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn(token);

        AuthResult result = authService.authenticateUserAndGenerateToken(request);

        assertThat(result.user()).isEqualTo(user);
        assertThat(result.token()).isEqualTo(token);
    }

    @Test
    void authenticateUserAndGenerateToken_shouldThrow_whenUserNotFound() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticateUserAndGenerateToken(request))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void authenticateUserAndGenerateToken_shouldThrow_whenAuthenticationFails() {
        LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.core.AuthenticationException("Bad credentials") {});

        assertThatThrownBy(() -> authService.authenticateUserAndGenerateToken(request))
                .isInstanceOf(org.springframework.security.core.AuthenticationException.class)
                .hasMessageContaining("Bad credentials");
    }
}