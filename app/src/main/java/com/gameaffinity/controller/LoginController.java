package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.model.UserRole;
import com.gameaffinity.service.JwtService;
import com.gameaffinity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public LoginController(JwtService jwtService, UserService userService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        UserBase user = userService.authenticate(email, password);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }

        String token = jwtService.generateToken(user.getId(), email, Collections.singletonList(user.getRole()));
        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole().toString()
        ));
    }
}
