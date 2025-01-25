package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
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
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update-role")
    @Operation(summary = "Actualizar rol de usuario", description = "Asigna un nuevo rol a un usuario.")
    public ResponseEntity<?> updateUserRole(@RequestBody UserBase user, @RequestParam String newRole) {
        boolean result = userService.updateUserRole(user, newRole);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Rol del usuario actualizado.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al actualizar el rol del usuario.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario de la base de datos.")
    public ResponseEntity<?> deleteUser(@RequestBody UserBase user) {
        boolean result = userService.deleteUser(user.getId());
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Usuario eliminado.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al eliminar al usuario.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }
}
