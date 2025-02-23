package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "API para la gestión de usuarios")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update-profile")
    @Operation(summary = "Actualizar perfil de usuario", description = "Actualiza la información del perfil del usuario.")
    public ResponseEntity<?> updateProfile(
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) String newPassword) {

        String emailFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean result = userService.updateUserProfile(emailFromToken, newName, newEmail, newPassword);

        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put(result ? "message" : "error", result ? "Perfil actualizado." : "Error al actualizar el perfil.");

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/getRole")
    @Operation(summary = "Obtener rol del usuario", description = "Devuelve el rol del usuario autenticado.")
    public ResponseEntity<?> getRole() {
        System.out.println("Hola");
        String emailFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        UserBase user = userService.getUserByEmail(emailFromToken);

        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of("role", user.getRole().toString()));
    }
}
