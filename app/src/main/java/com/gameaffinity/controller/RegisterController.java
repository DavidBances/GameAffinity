package com.gameaffinity.controller;

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
        String result = userService.registerUser(name, email, password, "REGULAR_USER");

        if ("Cuenta creada con éxito.".equals(result)) {
            return ResponseEntity.ok(Map.of("message", "Usuario registrado exitosamente. Por favor, inicia sesión."));
        }

        return ResponseEntity.badRequest().body(Map.of("error", result));
    }
}
