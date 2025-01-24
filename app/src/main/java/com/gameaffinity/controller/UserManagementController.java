package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if (users.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update-role")
    @Operation(summary = "Actualizar rol de usuario", description = "Asigna un nuevo rol a un usuario.")
    public ResponseEntity<?> updateUserRole(@RequestBody UserBase user, @RequestParam String newRole) {
        boolean result = userService.updateUserRole(user, newRole);
        if (result){
            return ResponseEntity.ok("{\"message\": \"Rol del usuario actualizado.\"}");
        }else{
            return ResponseEntity.badRequest().body("{\"error\": \"Error al actualizar el rol del usuario.\"}");
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario de la base de datos.")
    public ResponseEntity<?> deleteUser(@RequestBody UserBase user) {
        boolean result = userService.deleteUser(user.getId());
        if (result){
            return ResponseEntity.ok("{\"message\": \"Usuario eliminado.\"}");
        }else{
            return ResponseEntity.badRequest().body("{\"error\": \"Error al eliminar el usuario.\"}");
        }
    }
}
