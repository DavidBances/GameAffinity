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
        this.userService = userService; // Usa el constructor actual
    }

    @PutMapping("/update-profile")
    @Operation(summary = "Actualizar perfil de usuario", description = "Actualiza la información del perfil del usuario.")
    public ResponseEntity<?> updateProfile(
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) String newPassword
    ) {
        String emailFromToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean result = userService.updateUserProfile(emailFromToken, newName, newEmail, newPassword);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Perfil actualizado.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al actualizar el perfil.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/getRole")
    public ResponseEntity<?> getRole() {
        String emailFromToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserBase user = userService.getUserByEmail(emailFromToken);

        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(user.getRole());
    }
}