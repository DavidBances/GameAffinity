package com.gameaffinity.controller;

import com.gameaffinity.exception.UserNotFoundException;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.model.UserRole;
import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-management")
@PreAuthorize("hasAuthority('Administrator')")
@Tag(name = "User Management", description = "API para la gestión de usuarios")
public class UserManagementController {

    private final UserService userService;

    @Autowired
    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista con todos los usuarios.")
    public ResponseEntity<List<UserBase>> getAllUsers() {
        List<UserBase> users = userService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @PutMapping("/update-role")
    @Operation(summary = "Actualizar rol de usuario", description = "Asigna un nuevo rol a un usuario.")
    public ResponseEntity<?> updateUserRole(@RequestParam String userEmail, @RequestParam UserRole newRole) {
        try {
            boolean result = userService.updateUserRole(userEmail, newRole);

            Map<String, Object> response = new HashMap<>();
            response.put("success", result);
            response.put(result ? "message" : "error", result ? "Rol del usuario actualizado." : "Error al actualizar el rol del usuario.");

            return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rol no válido."));
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario de la base de datos.")
    public ResponseEntity<?> deleteUser(@RequestParam String userEmail) {
        boolean result = userService.deleteUser(userEmail);

        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put(result ? "message" : "error", result ? "Usuario eliminado." : "Error al eliminar al usuario.");

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
