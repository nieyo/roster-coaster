package com.github.nieyo.controller;

import com.github.nieyo.model.user.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO request) {

        // - Prüfen, ob die E-Mail schon existiert
        // - Passwort hashen
        // - User speichern

        return ResponseEntity.ok("Registrierung erfolgreich!");
    }

}
