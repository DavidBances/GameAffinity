package com.gameaffinity.controller;

import com.gameaffinity.exception.GameAffinityException;
import com.gameaffinity.model.UserRole;
import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/register")
@Tag(name = "User Registration", description = "API para la gestión del registro de usuarios")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario con nombre, email y contraseña.")
    public ResponseEntity<?> register(@RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String password) {

        try {
            userService.registerUser(name.trim(), email.trim(), password.trim());
        } catch (GameAffinityException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.ok(Map.of("message", "Usuario registrado exitosamente. Por favor, inicia sesión."));
    }
}
